package com.mtiteiu.clinic.repository;

import com.mtiteiu.clinic.model.patient.PatientDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<PatientDetails, Long> {
    Optional<PatientDetails> findPatientByPhoneNumber(String cnp);
}
