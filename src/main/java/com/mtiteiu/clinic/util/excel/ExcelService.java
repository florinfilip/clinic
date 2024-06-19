package com.mtiteiu.clinic.util.excel;

import com.mtiteiu.clinic.model.patient.Patient;
import jakarta.servlet.http.HttpServletResponse;

import java.util.List;

public interface ExcelService {
    void createExcel(List<Patient> patients, HttpServletResponse response);
}