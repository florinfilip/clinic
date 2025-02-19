package com.mtiteiu.clinic.repository;

import com.mtiteiu.clinic.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Optional<Person> findByPhoneNumber(String phoneNumber);
}
