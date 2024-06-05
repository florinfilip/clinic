package com.mtiteiu.clinic.service;

import com.mtiteiu.clinic.exception.DatabaseActionException;
import com.mtiteiu.clinic.exception.NotFoundException;
import com.mtiteiu.clinic.mapper.PatientDetailsMapper;
import com.mtiteiu.clinic.model.Person;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.repository.PatientRepository;
import com.mtiteiu.clinic.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PatientServiceImpl implements PatientService {
    private final UserRepository userRepository;

    private final PatientRepository patientRepository;

    @Override
    public List<Patient> getPatients() {
        return patientRepository.findAll();
    }

    @Override
    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Pacientul cu id-ul %s nu a fost gasit!", id)));
    }

    @Override
    public Patient createPatient(Patient patient) {

        patient.setPatientDetails(new PatientDetails());

        try {
            patientRepository.save(patient);
            log.info("Patient {} {} created successfully!", patient.getFirstName(), patient.getLastName());
            return patient;
        } catch (DataIntegrityViolationException ex) {
            String errorMessage = "Error while trying to save the patient. ";

            if (ex.getCause() instanceof ConstraintViolationException constraintViolationEx) {
                String columnName = constraintViolationEx.getConstraintName();
                errorMessage += String.format("Unique constraint violation on column: %s", columnName);
            } else {
                errorMessage += "A unique constraint violation occurred.";
            }

            log.error(errorMessage);
            throw new DatabaseActionException(errorMessage);
        } catch (Exception ex) {
            log.error(String.format("Error while trying to save the patient. Cause: %s", ex.getMessage()));

        }
        return patient;
    }

    @Override
    public String deletePatient(String cnp) {
        try {
            patientRepository.deleteByCnp(cnp);
        } catch (Exception ex) {
            log.error("Error occurred while trying to delete patient!");
            throw new DatabaseActionException("Erorr occurred while trying to delete patient from the database!");
        }

        return "Patient deleted successfully!";

    }

    @Transactional
    @Override
    public Patient updatePatient(Patient patient) {
        Patient newPatient = patientRepository.findById(patient.getId())
                .orElseThrow(() -> new NotFoundException(String.format("User with id %s not found!", patient.getId())));

        newPatient.setCnp(patient.getCnp());
        newPatient.setDateOfBirth(patient.getDateOfBirth());
        newPatient.setFirstName(patient.getFirstName());
        newPatient.setLastName(patient.getLastName());
        newPatient.setPhoneNumber(patient.getPhoneNumber());

        return patientRepository.save(newPatient);
    }

    @Transactional
    @Override
    public PatientDetails updatePatientDetails(MyUserDetails userDetails, PatientDetails updatedDetails) {

        Long id = userDetails.getUserId();

        Patient patient = patientRepository.findById(id).orElseThrow(() ->
                new NotFoundException(String.format("Patient with id %s not found!", id)));

        PatientDetails details = Optional.ofNullable(patient.getPatientDetails()).orElse(new PatientDetails());

        PatientDetailsMapper.INSTANCE.updatePatientDetails(updatedDetails, details);

        patient.setPatientDetails(details);
        patientRepository.save(patient);
        return details;
    }

    @Override
    public PatientDetails getPatientDetailsByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException(String.format("User with email %s not found!", email)));

        return getPersonPatientDetails(user.getPerson());
    }


    private <T extends Person> PatientDetails getPersonPatientDetails(T person) {
        if (person != null) {
            return person.getPatientDetails();
        } else {
            throw new NotFoundException("Person %s is null!");
        }
    }
}
