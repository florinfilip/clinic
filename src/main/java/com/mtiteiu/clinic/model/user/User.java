package com.mtiteiu.clinic.model.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mtiteiu.clinic.model.Person;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import java.util.Set;

import static com.mtiteiu.clinic.util.Constants.EMAIL_REGEX;

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
    @Size(min = 8, message = "The password must have at least 8 characters!")
    private String password;

    @NotNull(message = "Phone number cannot be null!")
    @Column(unique = true)
    private String phoneNumber;

    @NotNull(message = "Email cannot be null!")
    @Column(unique = true)
    @Email(regexp = EMAIL_REGEX, flags = Pattern.Flag.CASE_INSENSITIVE)
    private String email;

    private Boolean enabled;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_role",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @JsonIgnore
    @OneToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person person;

}
