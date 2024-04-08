package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.dao.UserRegistrationRequest;
import com.mtiteiu.clinic.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getUsers();

    User createUser(UserRegistrationRequest user);

    User updateUser(Long id, User user);

    User getUser(Long id);
}
