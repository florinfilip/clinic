package com.mtiteiu.clinic.repository;

import com.mtiteiu.clinic.model.patient.PatientDetails;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<PatientDetails, Long> {
    Optional<PatientDetails> findPatientByPhoneNumber(String cnp);
}
