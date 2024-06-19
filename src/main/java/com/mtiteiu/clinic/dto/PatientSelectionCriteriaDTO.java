package com.mtiteiu.clinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mtiteiu.clinic.model.patient.*;
import lombok.Data;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PatientSelectionCriteriaDTO {

    private Integer[] ageRange;
    private Integer[] heightRange;
    private Integer[] weightRange;
    private List<Race> races;
    private List<Religion> religions;
    private List<BloodType> bloodTypes;
    private List<CivilStatus> civilStatuses;
    private List<ProfessionalStatus> professionalStatuses;
    private List<Diet> diets;
    private Boolean smoker;
    private Boolean alcohol;
    private Boolean allergies;
    private Boolean conditions;


    public String getSmoker() {
        if (Boolean.TRUE.equals(smoker)) {
            return "Da";
        } else return "Nu";
    }

    public String getAlcohol() {
        if (Boolean.TRUE.equals(alcohol)) {
            return "Da";
        } else return "Nu";
    }
}
