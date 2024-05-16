package com.mtiteiu.clinic.model;

import com.mtiteiu.clinic.model.patient.Gender;
import com.mtiteiu.clinic.model.user.User;
import com.mtiteiu.clinic.validation.ValidCNP;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@SuperBuilder
@Table(name = "persons")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, name = "person_type")
@NoArgsConstructor
@AllArgsConstructor
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "person")
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
    private Gender gender;

    @NotNull
    @Column(unique = true)
    @Min(value = 10, message = "Please check your phone number!")
    private String phoneNumber;
}
