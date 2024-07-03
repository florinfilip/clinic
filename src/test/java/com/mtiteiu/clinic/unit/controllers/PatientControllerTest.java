package com.mtiteiu.clinic.unit.controllers;

import com.mtiteiu.clinic.controllers.PatientController;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.service.PatientService;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.Arrays;
import java.util.List;

import static com.mtiteiu.clinic.Constants.*;
import static com.mtiteiu.clinic.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.NO_CONTENT;
import static org.springframework.http.HttpStatus.OK;

@ExtendWith(MockitoExtension.class)
class PatientControllerTest {

    @Mock
    private PatientService patientService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private PatientController patientController;

    @Test
    void getPatients() {

        // given
        Pageable pageable = PageRequest.of(0, 2); // First page, 2 items per page
        List<Patient> patientList = Arrays.asList(createDefaultPatient(), createDefaultPatient());
        Page<Patient> patientPage = new PageImpl<>(patientList, pageable, patientList.size());

        when(patientService.getPatients(any(Pageable.class))).thenReturn(patientPage);

        // when
        ResponseEntity<Page<Patient>> response = patientController.getPatientsPage(pageable); // Ensure your controller method can accept Pageable as a parameter

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getNumberOfElements());
        assertEquals(2, response.getBody().getContent().size());
        assertEquals(patientList, response.getBody().getContent()); // Optionally check if the contents are as expected
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

    @Test
    void getPatientDetails_authenticated_returnsPatientDetails() {
        // given
        String email = "test@email.com";
        MyUserDetails mockUserDetails = createDefaultMyUserDetails();
        PatientDetails mockPatientDetails = new PatientDetails();
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        when(patientService.getPatientDetailsByEmail(email)).thenReturn(mockPatientDetails);

        // when
        ResponseEntity<PatientDetails> response = patientController.getPatientDetails(authentication);

        // then
        assertNotNull(response);
        assertTrue(response.getStatusCode().is2xxSuccessful());
        assertEquals(mockPatientDetails, response.getBody());
    }

    @Test
    void getPatientDetails_notAuthenticated_throws() {
        // given
        when(authentication.isAuthenticated()).thenReturn(false);

        // when
        ValidationException exception = assertThrows(ValidationException.class, () -> patientController.getPatientDetails(authentication));

        //then
        assertEquals("No authentication session found!", exception.getMessage());
    }

    @Test
    void getPatientDetails_nullAuthentication_throwsValidationException() {
        // given
        ValidationException exception = assertThrows(ValidationException.class, () -> patientController.getPatientDetails(null));

        //then
        assertEquals("No authentication session found!", exception.getMessage());
    }

    @Test
    void updatePatientDetails_validAuthentication_returnsUpdatedPatientDetails() {
        // given
        MyUserDetails mockUserDetails = mock(MyUserDetails.class);
        PatientDetails mockUpdatedDetails = new PatientDetails();
        PatientDetails mockResponseDetails = new PatientDetails();
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);
        when(patientService.updatePatientDetails(mockUserDetails, mockUpdatedDetails)).thenReturn(mockResponseDetails);

        // when
        ResponseEntity<PatientDetails> response = patientController.updatePatientDetails(authentication, mockUpdatedDetails);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockResponseDetails, response.getBody());
    }

    @Test
    void updatePatientDetails_nullAuthentication_throwsException() {
        // Arrange
        PatientDetails mockUpdatedDetails = new PatientDetails();

        // Act & Assert
        assertThrows(ValidationException.class, () -> patientController.updatePatientDetails(null, mockUpdatedDetails));
    }

    @Test
    void updatePatientDetails_nullUpdatedDetails_throws() {
        // when/then
        assertThrows(ValidationException.class, () -> patientController.updatePatientDetails(authentication, null));
    }
}
