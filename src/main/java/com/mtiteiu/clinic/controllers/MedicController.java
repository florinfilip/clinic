package com.mtiteiu.clinic.controllers;

import com.mtiteiu.clinic.dto.PatientSelectionCriteriaDTO;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.patient.PatientStatus;
import com.mtiteiu.clinic.service.PatientService;
import com.mtiteiu.clinic.util.excel.ExcelService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/medics")
@RequiredArgsConstructor
public class MedicController {

    private final ExcelService excelService;

    private final PatientService patientService;


    @PostMapping("/patients")
    public void fetchEligiblePatients(@RequestBody PatientSelectionCriteriaDTO criteria, HttpServletResponse response) {

        List<Patient> patientList = patientService.fetchEligiblePatients(criteria);

        response.setContentType("application/octet-stream");
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=Patients-List.xlsx";
        response.setHeader(headerKey, headerValue);
        response.setContentType("application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=Pacienti.xlsx");

        excelService.createExcel(patientList, response);
    }

    @PatchMapping("/patients/status/{id}")
    public ResponseEntity<String> updatePatientStatus(@PathVariable Long id, @RequestBody String status) {
        PatientStatus patientStatus = PatientStatus.fromValue(status);
        patientService.updatePatientStatus(id, patientStatus);
        return ResponseEntity.ok("Patient status updated successfully!");
    }


}
