package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.ValidationException;
import lombok.*;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "patientDetails")
    @JsonIgnoreProperties("patientDetails")
    private Patient patient;

    private Integer age;

    private Race race;

    private Double weight;

    private Double height;

    private BloodType bloodType;

    private Religion religion;

    @ElementCollection
    @CollectionTable(name = "allergies", joinColumns = @JoinColumn(name = "patient_id"))
    private List<String> allergies;

    public Integer getAge() {
        var dateOfBirth = patient.getDateOfBirth();
        if (dateOfBirth == null) {
            throw new ValidationException("Date of birth is null for patient with id %s!");
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
