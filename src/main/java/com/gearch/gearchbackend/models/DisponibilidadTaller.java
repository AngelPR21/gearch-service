package com.gearch.gearchbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.gearch.gearchbackend.enums.DiaSemana;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalTime;

// Modelo que representa el horario de un taller para un dia de la semana concreto
// Cada taller tiene un objeto DisponibilidadTaller por cada dia que trabaja
// La restriccion unique evita que haya dos horarios para el mismo taller y dia
@Entity
@Table(
    name = "disponibilidad_taller",
    uniqueConstraints = @UniqueConstraint(columnNames = {"taller_id", "dia_semana"})
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
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

    // Cada cuantos minutos hay un slot disponible. Por defecto 30 minutos
    @Column(name = "intervalo_minutos", nullable = false)
    @Builder.Default
    private Integer intervaloMinutos = 30;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taller_id", nullable = false)
    @JsonIgnore
    private Taller taller;
}
