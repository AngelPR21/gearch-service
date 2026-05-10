package com.gearch.gearchbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// Modelo que representa un taller mecanico en la base de datos
@Entity
@Table(name = "talleres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Taller {

    // La id se genera automaticamente al insertar en la base de datos
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String telefono;

    private String descripcion;

    // Coordenadas para buscar talleres cercanos con la query de distancia
    private Double latitud;
    private Double longitud;

    // Foto almacenada como bytes directamente en la base de datos
    // Si es null el frontend muestra una imagen por defecto
    @Lob
    @Column(name = "foto_perfil")
    private byte[] fotoPerfil;

    // Al borrar el taller se borran en cascada su disponibilidad, servicios, citas y resenas
    @OneToMany(mappedBy = "taller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<DisponibilidadTaller> disponibilidad = new ArrayList<>();

    @OneToMany(mappedBy = "taller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Servicio> servicios = new ArrayList<>();

    @OneToMany(mappedBy = "taller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Cita> citas = new ArrayList<>();

    @OneToMany(mappedBy = "taller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default
    private List<Resena> resenas = new ArrayList<>();
}
