package com.mtiteiu.clinic.model;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
}
