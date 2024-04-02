package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.NotFoundException;
import com.mtiteiu.clinic.dao.UserRegistrationRequest;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.model.user.Role;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.repository.PatientRepository;
import com.mtiteiu.clinic.repository.RoleRepository;
import com.mtiteiu.clinic.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
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
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return new MyUserDetails(userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No username with value %s found!")));
    }

    @Override
    public User createUser(UserRegistrationRequest request) {

        var defaultRole = roleRepository.findByName("USER").orElseGet(() -> new Role("USER"));

        var patientDetails = patientRepository.findPatientByPhoneNumber(request.getPhoneNumber())
                .orElseGet(() -> PatientDetails.builder()
                        .firstName(request.getFirstName())
                        .lastName(request.getLastName())
                        .phoneNumber(request.getPhoneNumber())
                        .build());

        var newUser = User.builder()
                .email(request.getEmail())
                .phoneNumber(request.getPhoneNumber())
                .password(bCryptPasswordEncoder.encode(request.getPassword()))
                .patient(patientDetails)
                .roles(Set.of(defaultRole)).build();

        try {
            return userRepository.save(newUser);
        } catch (Exception ex) {
            log.error("Error while trying to save user: {} \n {}", newUser.getEmail(), ex.getMessage());
            throw new NotFoundException(String.format("Error while trying to save user %s: %s", request.getEmail(), ex.getMessage()));
        }
    }

    @Override
    public User updateUser(User user) {
        var updatedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new NotFoundException(String.format("User with username %s not found!", user.getEmail())));

        updatedUser.setPassword(user.getPassword());
        updatedUser.setEmail(user.getEmail());
        userRepository.save(updatedUser);

        return userRepository.save(user);
    }
}
