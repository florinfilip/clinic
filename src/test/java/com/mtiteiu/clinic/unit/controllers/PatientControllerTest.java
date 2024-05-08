package com.mtiteiu.clinic.unit.controllers;

import com.mtiteiu.clinic.controllers.PatientController;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.service.PatientService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.mtiteiu.clinic.Constants.*;
import static com.mtiteiu.clinic.TestUtils.createDefaultPatient;
import static com.mtiteiu.clinic.TestUtils.createPatient;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @Test
    void getPatients() {

        //given
        List<Patient> patientList = Arrays.asList(createDefaultPatient(), createDefaultPatient());
        when(patientService.getPatients()).thenReturn(patientList);

        //when

        var response = patientController.getPatients();

        //then
        assertEquals(OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isEqualTo(2);
    }

    @Test
    void getPatientByEmail() {
        //given
        var patient = Patient.builder().firstName("test").build();
        when(patientService.getPatientById(anyLong())).thenReturn(patient);

        //when
        var response = patientController.getPatientByPhoneNumber(1L);

        //then
        assertNotNull(response);
        assertEquals(OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertThat(response.getBody().getFirstName()).isEqualTo("test");
    }

    @Test
    void updatePatient() {
        //given
        var updatedPatient = createPatient(1L, CNP, UPDATED_FIRST_NAME, UPDATED_LAST_NAME, UPDATED_PHONE_NO);
        when(patientService.updatePatient(any(Patient.class))).thenReturn(updatedPatient);
        //when
        var response = patientController.updatePatient(updatedPatient);
        //then

        assertThat(response).isNotNull();
        assertEquals(OK, response.getStatusCode());
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).extracting(Patient::getFirstName, Patient::getLastName, Patient::getPhoneNumber)
                .containsExactly(UPDATED_FIRST_NAME, UPDATED_LAST_NAME, UPDATED_PHONE_NO);
    }

    @Test
    void deletePatient_shouldDelete() {
        //given
        when(patientService.deletePatient(anyString())).thenReturn("Patient deleted!");

        //when
        var response = patientController.deletePatient(CNP);

        //then
        verify(patientService, times(1)).deletePatient(CNP);
        assertEquals(NO_CONTENT, response.getStatusCode());
        assertThat(response.getBody()).isEqualTo("Patient deleted!");
    }
}
