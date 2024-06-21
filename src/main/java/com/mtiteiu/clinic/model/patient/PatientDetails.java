package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.ValidationException;
import lombok.*;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.Period;

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
    @JsonIgnoreProperties({"patientDetails", "user"})
    private Patient patient;

    private Integer age;

    private Double weight;

    private Double height;

    private BloodType bloodType;

    private Religion religion;

    private Race race;

    private ProfessionalStatus professionalStatus;

    private CivilStatus civilStatus;

    private PregnancyStatus pregnancyStatus;

    private Diet diet;

    private String smoker;

    private String alcohol;

    private String bmi;

    //    @ElementCollection
//    @CollectionTable(name = "allergies", joinColumns = @JoinColumn(name = "patient_id"))
//    @Column(name = "allergy")
    private String allergies;

    //    @ElementCollection
//    @CollectionTable(name = "vices", joinColumns = @JoinColumn(name = "patient_id"))
    private String conditions;

    private String medications;

    public Integer getUserAge() {
        var dateOfBirth = patient.getDateOfBirth();
        if (dateOfBirth == null) {
            throw new ValidationException("Date of birth is null for patient with id %s!");
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }

    public String getBmi() {
        DecimalFormat df = new DecimalFormat("0.00");

        if (height == null || weight == null) {
            throw new ValidationException("Height and weight must be provided to calculate BMI.");
        }
        if (height <= 0 || weight <= 0) {
            throw new ValidationException("Height and weight must be greater than zero.");
        }
        return df.format((weight / (height * height) * 10000));
    }
}
