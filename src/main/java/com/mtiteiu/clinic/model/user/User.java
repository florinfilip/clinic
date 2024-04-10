package com.mtiteiu.clinic.model.user;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mtiteiu.clinic.model.patient.PatientDetails;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
@Entity
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "You must provide a password!")
    private String password;

    @NotNull(message = "Phone number cannot be null!")
    @Column(unique = true)
    private String phoneNumber;

    @NotNull(message = "Email cannot be null!")
    @Column(unique = true)
    private String email;

    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @JsonIgnoreProperties("user")
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private PatientDetails patient;

}
