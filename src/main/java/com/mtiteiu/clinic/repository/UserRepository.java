package com.mtiteiu.clinic.repository;

import com.mtiteiu.clinic.model.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByPhoneNumber(String phoneNumber);
    Optional<User> findByEmail(String email);
    Optional<User> findUserByEmail(String email);

    Boolean existsByEmail(String email);
}
