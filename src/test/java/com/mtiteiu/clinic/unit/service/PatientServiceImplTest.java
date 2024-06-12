package com.mtiteiu.clinic.unit.service;

import com.mtiteiu.clinic.exception.DatabaseActionException;
import com.mtiteiu.clinic.exception.NotFoundException;
import com.mtiteiu.clinic.model.patient.*;
import com.mtiteiu.clinic.model.user.MyUserDetails;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.repository.PatientRepository;
import com.mtiteiu.clinic.repository.UserRepository;
import com.mtiteiu.clinic.service.PatientServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static com.mtiteiu.clinic.Constants.*;
import static com.mtiteiu.clinic.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PatientServiceImplTest {

    @Mock
    private PatientRepository patientRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PatientServiceImpl patientService;


    @Test
    void testGetPatients() {
        //given
        when(patientRepository.findAll()).thenReturn(createDefaultPatientList());

        //when
        List<Patient> result = patientService.getPatients();


        //then
        assertEquals(4, result.size());
    }

    @Test
    void testGetPatientById_ExistingPatient() {

        //given
        var patient = createDefaultPatient();
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        //when
        Patient result = patientService.getPatientById(1L);

        //then
        assertNotNull(result);
        assertEquals(patient, result);
    }

    @Test
    void testGetPatientById_NonExistingPatient() {
        //given
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        //when/then
        Exception ex = assertThrows(NotFoundException.class, () -> patientService.getPatientById(1L));
        assertThat(ex.getMessage()).isEqualTo("Pacientul cu id-ul " + 1L + " nu a fost gasit!");
    }

    @Test
    void testCreatePatient() {
        //given
        Patient patient = createDefaultPatient();
        when(patientRepository.save(patient)).thenReturn(patient);

        //when
        Patient createdPatient = patientService.createPatient(patient);

        //then
        verify(patientRepository, times(1)).save(patient);
        assertNotNull(createdPatient);
        assertEquals(patient, createdPatient);
    }

    @Test
    void testDeletePatient_Success() {
        //given
        doNothing().when(patientRepository).deleteByCnp(CNP);

        //when
        String result = patientService.deletePatient(CNP);

        assertEquals("Patient deleted successfully!", result);
    }

    @Test
    void testDeletePatient_Exception() {
        //given
        doThrow(new RuntimeException("Database error")).when(patientRepository).deleteByCnp(CNP);

        //when/then
        Exception ex = assertThrows(DatabaseActionException.class, () -> patientService.deletePatient(CNP));
        assertThat(ex.getMessage()).isEqualTo("Erorr occurred while trying to delete patient from the database!");
    }


    @Test
    void testUpdatePatient_Success() {
        //given
        Patient existingPatient = createPatient(1L, CNP, FIRST_NAME, LAST_NAME, PHONE_NUMBER);
        Patient expectedResult = createPatient(1L, UPDATED_CNP, UPDATED_FIRST_NAME, UPDATED_LAST_NAME, UPDATED_PHONE_NO);

        when(patientRepository.findById(1L)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(expectedResult);

        //when
        Patient actualResult = patientService.updatePatient(expectedResult);

        //then
        assertNotNull(actualResult);
        assertEquals(expectedResult.getFirstName(), actualResult.getFirstName());
        assertEquals(expectedResult.getLastName(), actualResult.getLastName());
        assertEquals(expectedResult.getPhoneNumber(), actualResult.getPhoneNumber());
    }

    @Test
    void testUpdatePatient_NotFound() {

        //given
        Patient updatedPatient = createDefaultPatient();

        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        //when/then
        assertThrows(NotFoundException.class, () -> patientService.updatePatient(updatedPatient));
    }

    @Test
    void testUpdatePatientDetails_success() {
        //given

        PatientDetails updatedDetails = createDefaultPatientDetails();
        MyUserDetails myUserDetails = createDefaultMyUserDetails();

        Patient patient = createDefaultPatient();

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        //when
        PatientDetails result = patientService.updatePatientDetails(myUserDetails, updatedDetails);

        //then
        assertNotNull(result);
        assertEquals(updatedDetails.getAge(), result.getAge());
    }

    @Test
    void testUpdatePatientDetails_patientNotFound() {
        //given
        PatientDetails updatedDetails = createDefaultPatientDetails();

        //when.then
        when(patientRepository.findById(anyLong())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> patientService.updatePatientDetails(createDefaultMyUserDetails(), updatedDetails));
    }


    @Test
    void updatePatientDetails_patientExists_returnsUpdatedDetails() {
        // given
        MyUserDetails userDetails = createDefaultMyUserDetails();
        Patient patient = createDefaultPatient();
        PatientDetails updatedDetails = createPatientDetails(
                18,
                BloodType.AB_NEGATIVE,
                Race.HISPANIC,
                Religion.ORTODOX,
                170.00,
                70.00);


        when(patientRepository.findById(anyLong())).thenReturn(Optional.of(patient));
        when(patientRepository.save(patient)).thenReturn(patient);

        // when
        PatientDetails result = patientService.updatePatientDetails(userDetails, updatedDetails);

        // then
        assertNotNull(result);
        verify(patientRepository).save(patient);
    }

    @Test
    void updatePatientDetails_PatientNotFound_ThrowsNotFoundException() {
        // given
        Long userId = 1L;
        MyUserDetails userDetails = createDefaultMyUserDetails();
        PatientDetails updatedDetails = createPatientDetails(
                18,
                BloodType.AB_NEGATIVE,
                Race.HISPANIC,
                Religion.ORTODOX,
                170.00,
                70.00);

        when(patientRepository.findById(userId)).thenReturn(Optional.empty());

        // when/then
        assertThrows(NotFoundException.class, () -> patientService.updatePatientDetails(userDetails, updatedDetails));
    }

    // Tests for getPatientDetailsByEmail
    @Test
    void getPatientDetailsByEmail_userExists_returnsPatientDetails() {
        // given

        User user = createDefaultUser();
        PatientDetails patientDetails = user.getPerson().getPatientDetails();

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // when
        PatientDetails result = patientService.getPatientDetailsByEmail(user.getEmail());

        // then
        assertNotNull(result);
        assertEquals(patientDetails, result);
    }

    @Test
    void getPatientDetailsByEmail_userNotFound_throwsNotFoundException() {
        // given
        String email = "user@example.com";

        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // when/then
        assertThrows(NotFoundException.class, () -> patientService.getPatientDetailsByEmail(email));
    }
}
