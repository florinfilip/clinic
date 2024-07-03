package com.mtiteiu.clinic.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mtiteiu.clinic.model.patient.*;
import lombok.Data;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PatientSelectionCriteriaDTO {

    private Integer[] ageRange;
    private Integer[] heightRange;
    private Integer[] weightRange;
    private Integer[] bmiRange;

    private PregnancyStatus pregnancyStatus;

    private List<Race> races;
    private List<Religion> religions;
    private List<BloodType> bloodTypes;
    private List<CivilStatus> civilStatuses;
    private List<ProfessionalStatus> professionalStatuses;
    private List<Diet> diets;
    private Boolean smoker;
    private Boolean alcohol;

    private Map<String, Boolean> allergies;
    private Map<String, Boolean> conditions;
    private Map<String, Boolean> medications;


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
