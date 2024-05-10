package com.mtiteiu.clinic.repository;

import com.mtiteiu.clinic.model.patient.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {
    void deleteByCnp(String cnp);

    Optional<Patient> findPatientByPhoneNumber(String phoneNumer);

}
