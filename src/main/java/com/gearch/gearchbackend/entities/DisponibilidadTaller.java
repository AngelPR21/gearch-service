package com.gearch.gearchbackend.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.gearch.gearchbackend.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalTime;

/**
 * Define el horario semanal de un taller.
 * El admin configura qué días trabaja y en qué franja horaria.
 * Ejemplo: Taller 1 trabaja los LUNES de 08:30 a 18:00.
 *
 * A partir de esta tabla, Android puede calcular qué horas
 * están disponibles para reservar en un día concreto,
 * descartando las que ya tienen una Cita reservada.
 */
@Entity
@Table(
    name = "disponibilidad_taller",
    // Un taller no puede tener dos registros para el mismo día
    uniqueConstraints = @UniqueConstraint(columnNames = {"taller_id", "dia_semana"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DisponibilidadTaller {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "dia_semana", nullable = false)
    private DiaSemana diaSemana;

    @Column(name = "hora_inicio", nullable = false)
    private LocalTime horaInicio;

    @Column(name = "hora_fin", nullable = false)
    private LocalTime horaFin;

    // Intervalo entre citas en minutos (por defecto 30)
    // Ej: si es 30, las citas disponibles serían 09:00, 09:30, 10:00...
    @Column(name = "intervalo_minutos", nullable = false)
    @Builder.Default
    private Integer intervaloMinutos = 30;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taller_id", nullable = false)
    @JsonIgnore
    private Taller taller;
}
