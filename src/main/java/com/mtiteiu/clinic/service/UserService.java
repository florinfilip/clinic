package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getUsers();

    void createUser(User user);

}
