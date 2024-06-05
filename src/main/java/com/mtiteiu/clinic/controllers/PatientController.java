package com.mtiteiu.clinic.controllers;

import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.service.PatientService;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/patients")
public class PatientController {

    private final PatientService patientService;

    @GetMapping
    public ResponseEntity<List<Patient>> getPatients() {
        return ResponseEntity.ok().body(patientService.getPatients());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Patient> getPatientByPhoneNumber(@PathVariable Long id) {
        return ResponseEntity.ok().body(patientService.getPatientById(id));
    }

    @GetMapping("/details")
    public ResponseEntity<PatientDetails> getPatientDetails(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ValidationException("No authentication session found!");
        }

        UserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(patientService.getPatientDetailsByEmail(userDetails.getUsername()));
    }

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patient));
    }

    @PutMapping
    public ResponseEntity<Patient> updatePatient(@RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.updatePatient(patient));
    }

    @PatchMapping
    public ResponseEntity<PatientDetails> updatePatientDetails(Authentication authentication, @RequestBody PatientDetails updatedDetails) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ValidationException("No authentication session found!");
        }
        MyUserDetails userDetails = (MyUserDetails) authentication.getPrincipal();
        return ResponseEntity.status(HttpStatus.OK).body(patientService.updatePatientDetails(userDetails, updatedDetails));
    }

    @DeleteMapping
    public ResponseEntity<String> deletePatient(@RequestParam String cnp) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(patientService.deletePatient(cnp));
    }
}

