package com.gearch.gearchbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gearch.gearchbackend.enums.EstadoCita;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@Entity
@Table(
    name = "citas",
    uniqueConstraints = @UniqueConstraint(columnNames = {"taller_id", "fecha_hora"})
        //Esto una restriccion en la base de datos que dice que no puede haber dos filas con el mismo taller_id y fecha_hora a la vez.
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) //esto evita que json lea unas cosas que da el lazy y asi puede serializarlo y no peta
public class Cita {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "fecha_hora", nullable = false)
    private LocalDateTime fechaHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private EstadoCita estado = EstadoCita.CONFIRMADA;

    private String notas;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)
    @JsonIgnore
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "taller_id", nullable = false)
    @JsonIgnore
    private Taller taller;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servicio_id", nullable = false)
    @JsonIgnore
    private Servicio servicio;

    // Vehículo es opcional
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehiculo_id")
    @JsonIgnore
    private Vehiculo vehiculo;
}
