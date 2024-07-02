package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.dto.PatientSelectionCriteriaDTO;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.model.patient.PatientStatus;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PatientService {

    Page<Patient> getPatients(Pageable pageable);

    Patient getPatientById(Long id);

    Patient createPatient(Patient patient);

    String deletePatient(String cnp);

    Patient updatePatient(Patient patient);

    @Transactional
    PatientDetails updatePatientDetails(MyUserDetails userDetails, PatientDetails updatedDetails);

    @Transactional
    void updatePatientStatus(Long id, PatientStatus status);

    PatientDetails getPatientDetailsByEmail(String email);

    List<Patient> fetchEligiblePatients(PatientSelectionCriteriaDTO criteria);
}
