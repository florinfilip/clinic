package com.mtiteiu.clinic.model.patient;

import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.validation.ValidCNP;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "patients")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PatientDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
//    @PrimaryKeyJoinColumn
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @ValidCNP
    @Column(unique = true)
    private String cnp;

    private LocalDateTime dateOfBirth;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Column(unique = true)
    @Min(value = 10, message = "Please check your phone number")
    private String phoneNumber;

    private Integer age;

    private Gender gender;

    private Race race;

    private Double weight;

    private Double height;

    private BloodType bloodType;

    private Religion religion;

    @OneToMany(mappedBy = "patientDetails")
    private List<Allergy> allergies;

    @Entity
    @Data
    @Table(name = "allergies")
    public static class Allergy {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        String name;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "patient_id")
        PatientDetails patientDetails;

    }

}
