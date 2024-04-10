package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.dao.UserDTO;
import com.mtiteiu.clinic.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getUsers();

    User createUser(UserDTO user);

    User updateUser(Long id, User user);

    User getUserById(Long id);

    String deleteUserById(Long id);
}
