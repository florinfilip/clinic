package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mtiteiu.clinic.model.Person;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@JsonIgnoreProperties(ignoreUnknown = true)
@DiscriminatorValue("PATIENT")
public class Patient extends Person {

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("patient")
    private PatientDetails patientDetails;
}
