package com.authentication.module.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity(name = "category")
@Table(name = "category")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private String description;

    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL)
    private Set<News> noticias;
}
