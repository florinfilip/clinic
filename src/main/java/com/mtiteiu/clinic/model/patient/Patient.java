package com.mtiteiu.clinic.model.patient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.validation.ValidCNP;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;


@Entity
@Getter
@Setter
@Table(name = "patients")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "patient")
    private User user;

    @NotNull
    private LocalDate dateOfBirth;

    @ValidCNP
    @NotNull
    @Column(unique = true)
    private String cnp;

    @NotNull
    private String firstName;

    @NotNull
    private String lastName;

    @NotNull
    @Column(unique = true)
    @Min(value = 10, message = "Please check your phone number!")
    private String phoneNumber;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnoreProperties("patient")
    private PatientDetails patientDetails;
}
