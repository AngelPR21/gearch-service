package com.gearch.gearchbackend.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "Calendario")
public class Calendario {

    @Id
    @Column(name = "id_calendario")
    private Integer id;

    private LocalDate fecha_cita;
    private LocalTime hora_inicio;
    private Boolean disponible;

    @ManyToOne
    @JoinColumn(name = "id_taller")
    private Taller taller;

    // getters y setters
}
