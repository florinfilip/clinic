package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.exception.DatabaseActionException;
import com.mtiteiu.clinic.exception.NotFoundException;
import com.mtiteiu.clinic.exception.RetrievalException;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.model.user.Role;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.repository.PatientRepository;
import com.mtiteiu.clinic.repository.RoleRepository;
import com.mtiteiu.clinic.repository.UserRepository;
import jakarta.persistence.EntityManager;
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

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EntityManager entityManager;
    private final PatientRepository patientRepository;
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
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found!", id)));
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new MyUserDetails(userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("No username with value %s found!", email))));
    }

    @Override
    public User createUser(UserDTO request) {

        checkUniqueEmail(request.getEmail());

        var defaultRole = roleRepository.findByName("USER").orElseGet(() -> new Role("USER"));

        checkValidPassword(request);

        var patientDetails = patientRepository.findPatientByPhoneNumber(request.getPhoneNumber())
                .orElseGet(() -> Patient.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .phoneNumber(request.getPhoneNumber())
                        .build());

        var newUser = User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .patient(patientDetails)
                .enabled(true)
                .roles(Set.of(defaultRole)).build();

        patientDetails.setUser(newUser);

        try {
            userRepository.save(newUser);
            log.info("User {} created successfully!", newUser.getEmail());
            return newUser;
        } catch (Exception ex) {
            log.error("Error while trying to save user: {} \n {}", newUser.getEmail(), ex.getMessage());
            throw new DatabaseActionException(String.format("Error while trying to save user %s: %s", request.getEmail(), ex.getMessage()));
        }
    }

    private void checkUniqueEmail(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ValidationException(String.format("Email %s is already registered!", email));
        }
    }

    @Override
    public User updateUser(Long id, User updateDetails) {
        var updatedUser = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found!", id)));

        updatedUser.setPassword(updateDetails.getPassword() != null ? bCryptPasswordEncoder.encode(updateDetails.getPassword()) : updatedUser.getPassword());
        updatedUser.setPhoneNumber(updateDetails.getPhoneNumber());
        updatedUser.setEmail(updateDetails.getEmail());
        log.info("User with id {} updated successfully!", id);
        return userRepository.save(updatedUser);
    }

    @Override
    public String deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return String.format("User with id %s deleted successfully!", id);
        }
        throw new NotFoundException(String.format("User with id %s does not exist!", id));
    }

    private void checkValidPassword(UserDTO user) {
        if (!Objects.equals(user.getPassword(), user.getRepeatPassword())) {
            throw new ValidationException("Passwords do not match!");
        }
    }
}
