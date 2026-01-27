package com.gearch.gearchbackend.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "Resena")
public class Resena {

    @Id
    @Column(name = "id_resena")
    private Integer id;

    private String comentario;
    private Integer calificacion;
    private LocalDate fecha;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_taller")
    private Taller taller;

    // getters y setters
}
