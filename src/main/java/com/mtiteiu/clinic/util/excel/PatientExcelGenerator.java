package com.mtiteiu.clinic.util.excel;

import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@Slf4j
public class PatientExcelGenerator implements ExcelService {
    String[] headers = {
            "Nume", "Prenume", "Zi de naștere", "Gen", "Telefon", "E-mail",
            "Grupă sanguină", "Religie", "Rasă", "Statut Profesional", "Statut Civil", "Dietă", "Indice Masă Corporală",
            "Fumător", "Consum Alcool", "Alergii", "Afecțiuni", "Tratamente"};

    public void createExcel(List<Patient> patients, HttpServletResponse response) {
        try (XSSFWorkbook workbook = new XSSFWorkbook()) {
            XSSFSheet sheet = workbook.createSheet("Patients");

            CellStyle headerStyle = createHeaderStyle(workbook);
            CellStyle dataStyle = createDataStyle(workbook);

            writeHeaderLine(sheet, headerStyle);

            var filteredPatients = patients.stream()
                    .filter((user -> user.getUser() != null))
                    .toList();

            writeDataLines(filteredPatients, sheet, dataStyle);
            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
                sheet.setColumnWidth(i, sheet.getColumnWidth(i) + 2000);
            }

            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException ex) {
            log.error("Error occurred while trying to generate excel file: {}", ex.getMessage());
        }
    }

    private CellStyle createHeaderStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);

        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeightInPoints((short) 14);
        style.setFont(font);
        style.setAlignment(HorizontalAlignment.CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        return style;
    }

    private CellStyle createDataStyle(XSSFWorkbook workbook) {
        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        style.setAlignment(HorizontalAlignment.CENTER);
        font.setFontHeightInPoints((short) 13);
        style.setFont(font);
        return style;
    }

    private void writeHeaderLine(XSSFSheet sheet, CellStyle style) {
        int rowCount = 0;
        Row headerRow = sheet.createRow(rowCount);
        for (String header : headers) {
            createCell(headerRow, rowCount++, header, style);
        }
    }

    private void writeDataLines(List<Patient> patients, XSSFSheet sheet, CellStyle style) {
        int rowCount = 1;
        for (Patient patient : patients) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            PatientDetails patientDetails = patient.getPatientDetails();

            createCell(row, columnCount++, patient.getFirstName(), style);
            createCell(row, columnCount++, patient.getLastName(), style);
            createCell(row, columnCount++, patient.getDateOfBirth(), style);
            createCell(row, columnCount++, patient.getGender().getDisplayName(), style);
            createCell(row, columnCount++, patient.getPhoneNumber(), style);
            createCell(row, columnCount++, patient.getUser().getEmail(), style);
            createCell(row, columnCount++, patientDetails.getBloodType().getDisplayName(), style);
            createCell(row, columnCount++, patientDetails.getReligion().getDisplayName(), style);
            createCell(row, columnCount++, patientDetails.getRace().getDisplayName(), style);
            createCell(row, columnCount++, patientDetails.getProfessionalStatus().getDisplayName(), style);
            createCell(row, columnCount++, patientDetails.getCivilStatus().getStatus(), style);
            createCell(row, columnCount++, patientDetails.getDiet().getDisplayName(), style);
            createCell(row, columnCount++, patientDetails.getBmi(), style);
            createCell(row, columnCount++, patientDetails.getSmoker(), style);
            createCell(row, columnCount++, patientDetails.getAlcohol(), style);
            createCell(row, columnCount++, patientDetails.getAllergies(), style);
            createCell(row, columnCount++, patientDetails.getConditions(), style);
            createCell(row, columnCount, patientDetails.getMedications(), style);
        }
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        if (value == null) {
            value = "";
        }
        Cell cell = row.createCell(columnCount);
        switch (value.getClass().getSimpleName()) {
            case "Long" -> cell.setCellValue((Long) value);
            case "Integer" -> cell.setCellValue((Integer) value);
            case "Boolean" -> cell.setCellValue((Boolean) value);
            default -> cell.setCellValue(String.valueOf(value));
        }
        cell.setCellStyle(style);
    }
}

