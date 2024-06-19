package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.exception.DatabaseActionException;
import com.mtiteiu.clinic.exception.NotFoundException;
import com.mtiteiu.clinic.exception.RetrievalException;
import com.mtiteiu.clinic.mapper.UserMapper;
import com.mtiteiu.clinic.model.Person;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.model.user.Role;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.repository.PatientRepository;
import com.mtiteiu.clinic.repository.PersonRepository;
import com.mtiteiu.clinic.repository.RoleRepository;
import com.mtiteiu.clinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.ValidationException;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.mtiteiu.clinic.model.user.UserRoles.USER;
import static com.mtiteiu.clinic.util.Utils.getBirthDateFromCNP;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final PatientRepository patientRepository;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PersonRepository personRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public List<User> getUsers() {

        try {
            return userRepository.findAll();
        } catch (Exception ex) {
            log.error("Internal error: Could not fetch the users!");
            throw new RetrievalException("Could not fetch users from database!");
        }
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User-ul cu id %s nu a fost gasit!", id)));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new MyUserDetails(userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User-ul cu email %s nu a fost gasit!", email))));
    }

    @Override
    public User createUser(UserDTO request) {

        checkUniqueEmail(request.getEmail());

        var defaultRole = roleRepository.findByName(USER.name()).orElseGet(() -> new Role(USER.name()));

        checkValidPassword(request);

        Person patient = patientRepository.findPatientByPhoneNumber(request.getPhoneNumber())
                .orElse(Patient.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .cnp(request.getCnp())
                        .gender(request.getGender())
                        .dateOfBirth(getBirthDateFromCNP(request.getCnp()))
                        .phoneNumber(request.getPhoneNumber())
                        .build());

        var newUser = User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(bCryptPasswordEncoder.encode(request.getNewPassword()))
                .person(patient)
                .enabled(true)
                .roles(Set.of(defaultRole)).build();

        patient.setUser(newUser);

        try {
            userRepository.save(newUser);
            log.info("User-ul {} a fost creat cu succes!", newUser.getEmail());
            return newUser;
        } catch (Exception ex) {
            log.error("Error while trying to save user: {} \n {}", newUser.getEmail(), ex.getMessage());
            throw new DatabaseActionException(String.format("Error while trying to save user %s: %s", request.getEmail(), ex.getMessage()));
        }
    }

    @Transactional
    @Override
    public User updateUser(MyUserDetails userDetails, UserDTO updateDetails) {
        Long id = userDetails.getUserId();
        User updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User-ul cu id %s nu a fost gasit!", id)));

        checkUniqueFieldsValidity(userDetails, updateDetails);
        checkValidPassword(updateDetails);

        UserMapper.INSTANCE.updateUserDetails(updateDetails, updatedUser, bCryptPasswordEncoder);

        try {
            var user = userRepository.save(updatedUser);
            log.info("User-ul cu id {} modificat cu succes!", id);
            return user;
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }

        throw new RuntimeException("Error");
    }


    @Override
    public String deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return String.format("User-ul cu id %s a fost sters cu succes!", id);
        }
        throw new NotFoundException(String.format("Userul cu id-ul %s nu exista!", id));
    }

    private void checkUniqueFieldsValidity(MyUserDetails userDetails, UserDTO updateDetails) {
        if (!Objects.equals(userDetails.getUsername(), updateDetails.getEmail())) {
            checkUniqueEmail(updateDetails.getEmail());
        }
        if (!Objects.equals(userDetails.getPhoneNumber(), updateDetails.getPhoneNumber())) {
            checkUniquePhoneNumber(updateDetails.getPhoneNumber());
        }
        if (!Objects.equals(userDetails.getCNP(), updateDetails.getCnp())) {
            checkUniqueCNP(updateDetails.getCnp());
        }
    }

    private void checkValidPassword(UserDTO updateDetails) {

        var newPassword = updateDetails.getNewPassword();
        var confirmPassword = updateDetails.getConfirmPassword();
        var currentPassword = updateDetails.getCurrentPassword();

        if (newPassword == null && confirmPassword == null && currentPassword == null) {
            return;
        }

        if (updateDetails.getCurrentPassword() == null) {
            updateDetails.setCurrentPassword("");
        }
        if (!Objects.equals(newPassword, confirmPassword)) {
            throw new ValidationException("Parolele nu se potrivesc!");
        }

        if (bCryptPasswordEncoder.matches(newPassword, updateDetails.getCurrentPassword())) {
            throw new ValidationException("Parola trebuie sa fie diferita de cea precedenta!");
        }
    }

    private void checkUniqueEmail(String email) {
        if (Boolean.TRUE.equals(userRepository.existsByEmail(email))) {
            throw new ValidationException(String.format("Adresa de e-mail %s este deja inregistrata!", email));
        }
    }

    private void checkUniquePhoneNumber(String phoneNumber) {
        if (Boolean.TRUE.equals(userRepository.existsByPhoneNumber(phoneNumber))) {
            throw new ValidationException(String.format("Numarul de telefon %s este deja inregistrat!", phoneNumber));
        }
    }

    private void checkUniqueCNP(String cnp) {
        if (Boolean.TRUE.equals(userRepository.existsByPhoneNumber(cnp))) {
            throw new ValidationException(String.format("CNP-ul %s este deja inregistrat!", cnp));
        }
    }
}
