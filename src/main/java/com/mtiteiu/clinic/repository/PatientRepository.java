package com.mtiteiu.clinic.repository;

import com.mtiteiu.clinic.dto.PatientSelectionCriteriaDTO;
import com.mtiteiu.clinic.model.patient.Patient;
import com.mtiteiu.clinic.util.Utils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {
    void deleteByCnp(String cnp);

    Optional<Patient> findPatientByPhoneNumber(String phoneNumber);

    String PATIENT_DETAILS = "patientDetails";

    static Specification<Patient> createFilterSpecs(PatientSelectionCriteriaDTO criteria) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Add predicates based on range criteria
            addRangePredicate(root, cb, predicates, "age", criteria.getAgeRange());
            addRangePredicate(root, cb, predicates, "weight", criteria.getWeightRange());
            addRangePredicate(root, cb, predicates, "height", criteria.getHeightRange());

            // Add predicates based on enum criteria
            addInPredicate(root, predicates, "race", criteria.getRaces());
            addInPredicate(root, predicates, "religion", criteria.getReligions());
            addInPredicate(root, predicates, "bloodType", criteria.getBloodTypes());
            addInPredicate(root, predicates, "civilStatus", criteria.getCivilStatuses());
            addInPredicate(root, predicates, "professionalStatus", criteria.getProfessionalStatuses());
            addInPredicate(root, predicates, "diet", criteria.getDiets());

            addConditionPredicate(root, cb, predicates, "allergies", criteria.getAllergies());
            addConditionPredicate(root, cb, predicates, "conditions", criteria.getConditions());
            addConditionPredicate(root, cb, predicates, "medications", criteria.getMedications());

            // Add predicates based on boolean criteria
            addEqualPredicate(root, cb, predicates, "smoker", criteria.getSmoker());
            addEqualPredicate(root, cb, predicates, "alcohol", criteria.getAlcohol());

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    private static void addRangePredicate(Root<Patient> root,
                                          CriteriaBuilder cb,
                                          List<Predicate> predicates,
                                          String attributeName,
                                          Integer[] range) {
        if (range != null && range.length == 2) {
            predicates.add(cb.between(root.get(PATIENT_DETAILS).get(attributeName), range[0], range[1]));
        }
    }

    private static <T extends Enum<T>> void addInPredicate(Root<Patient> root,
                                                           List<Predicate> predicates,
                                                           String attributeName,
                                                           List<T> enumValues) {
        if (isNotNullOrEmpty(enumValues)) {
            predicates.add(root.get(PATIENT_DETAILS).get(attributeName).in(enumValues));
        }
    }

    private static void addEqualPredicate(Root<Patient> root,
                                          CriteriaBuilder cb,
                                          List<Predicate> predicates,
                                          String attributeName,
                                          String value) {
        if (Utils.isNotNullOrEmpty(value)) {
            if ("Da".equals(value)) {

                Predicate daPredicate = cb.equal(cb.lower(root.get(PATIENT_DETAILS).get(attributeName)), "da");
                Predicate ocazionalPredicate = cb.equal(cb.lower(root.get(PATIENT_DETAILS).get(attributeName)), "ocazional");
                predicates.add(cb.or(daPredicate, ocazionalPredicate));
            } else {

                predicates.add(cb.equal(cb.lower(root.get(PATIENT_DETAILS).get(attributeName)), "nu"));
            }
        }
    }

    private static void addConditionPredicate(Root<Patient> root,
                                              CriteriaBuilder cb,
                                              List<Predicate> predicates,
                                              String attributeName,
                                              Map<String, Boolean> values) {
        if (values != null) {
            values.forEach((key, include) -> {
                String[] terms = key.split(",");
                for (String term : terms) {
                    term = term.trim().toLowerCase();
                    Path<String> path = root.get(PATIENT_DETAILS).get(attributeName);
                    Predicate condition;
                    if (Boolean.TRUE.equals(include)) {
                        condition = cb.like(cb.lower(path), "%" + term + "%");
                    } else {
                        Predicate notLike = cb.notLike(cb.lower(path), "%" + term + "%");
                        Predicate isNull = cb.isNull(path);
                        condition = cb.or(notLike, isNull); // Exclude or handle null
                    }
                    predicates.add(condition);
                }
            });
        }
    }

    private static boolean isNotNullOrEmpty(Collection<?> collection) {
        return collection != null && !collection.isEmpty();
    }
}

