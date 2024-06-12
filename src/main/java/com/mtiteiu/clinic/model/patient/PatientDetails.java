package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.ValidationException;
import lombok.*;

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

    private Diet diet;

    private String smoker;

    private String alcohol;


    //    @ElementCollection
//    @CollectionTable(name = "allergies", joinColumns = @JoinColumn(name = "patient_id"))
    private String allergies;

    //    @ElementCollection
//    @CollectionTable(name = "vices", joinColumns = @JoinColumn(name = "patient_id"))
    private String conditions;

    //    @ElementCollection
//    @CollectionTable(name = "chronic_conditions", joinColumns = @JoinColumn(name = "patient_id"))
    private String chronicConditions;

    public Integer getUserAge() {
        var dateOfBirth = patient.getDateOfBirth();
        if (dateOfBirth == null) {
            throw new ValidationException("Date of birth is null for patient with id %s!");
        }
        return Period.between(dateOfBirth, LocalDate.now()).getYears();
    }
}
