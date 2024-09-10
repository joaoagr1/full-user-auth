package com.authentication.module.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity(name = "favorites")
@Table(name = "favorites")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Favorites {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private User usuario;

    @ManyToOne
    @JoinColumn(name = "noticia_id")
    private News noticia;
}
