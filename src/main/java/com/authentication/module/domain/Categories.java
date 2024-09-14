package com.authentication.module.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "categories")
@Table(name = "categories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categories {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String description;


}
