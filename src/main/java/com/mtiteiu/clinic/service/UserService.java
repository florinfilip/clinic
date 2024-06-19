package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.dto.UserDTO;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.model.user.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService {

    List<User> getUsers();

    User createUser(UserDTO user);

    User updateUser(MyUserDetails userDetails, UserDTO updateDetails);

    User getUserById(Long id);

    String deleteUserById(Long id);
}
