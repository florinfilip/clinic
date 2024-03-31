package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.model.MyUserDetails;
import com.mtiteiu.clinic.model.User;
import com.mtiteiu.clinic.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Data
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

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
    public void createUser(User user) {
        try {
            userRepository.save(user);
        } catch (Exception ex) {
            log.error("Error while trying to save user: {}", user);
        }
    }
}
