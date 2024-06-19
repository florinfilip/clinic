package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.dto.PatientSelectionCriteriaDTO;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import jakarta.transaction.Transactional;

import java.util.List;

public interface PatientService {

    List<Patient> getPatients();

    Patient getPatientById(Long id);

    Patient createPatient(Patient patient);

    String deletePatient(String cnp);

    Patient updatePatient(Patient patient);

    @Transactional
    PatientDetails updatePatientDetails(MyUserDetails userDetails, PatientDetails updatedDetails);

    PatientDetails getPatientDetailsByEmail(String email);

    List<Patient> fetchEligiblePatients(PatientSelectionCriteriaDTO criteria);
}
