package com.mtiteiu.clinic.controllers;

import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.service.PatientService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    public ResponseEntity<Patient> createPatient(@RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.CREATED).body(patientService.createPatient(patient));
    }

    @PutMapping
    public ResponseEntity<Patient> updatePatient(@RequestBody Patient patient) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.updatePatient(patient));
    }

    @PatchMapping
    public ResponseEntity<PatientDetails> updatePatientDetails(@RequestBody PatientDetails updatedDetails) {
        return ResponseEntity.status(HttpStatus.OK).body(patientService.updatePatientDetails(updatedDetails));
    }

    @DeleteMapping
    public ResponseEntity<String> deletePatient(@RequestParam String cnp) {
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(patientService.deletePatient(cnp));
    }
}

