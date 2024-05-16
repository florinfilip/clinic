package com.mtiteiu.clinic.model.medic;

import com.mtiteiu.clinic.model.Person;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MEDIC")
public class Medic extends Person {


}
