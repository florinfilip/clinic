package com.mtiteiu.clinic.model.medic;

import com.mtiteiu.clinic.model.Person;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("MEDIC")
public class Medic extends Person {


    private String specialization;

    @OneToOne
    private PatientDetails patientDetails;

}
