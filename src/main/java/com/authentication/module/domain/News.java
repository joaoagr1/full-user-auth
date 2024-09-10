package com.authentication.module.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;

    @Lob
    private String conteudo;

    private LocalDateTime dataPublicacao;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Category categoria;
}
