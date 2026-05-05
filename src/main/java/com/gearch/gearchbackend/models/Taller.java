package com.gearch.gearchbackend.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "talleres")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Taller {
//la id se genera solo cuando se inserta en  la bbdd
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

    private Double latitud;
    private Double longitud;

    @Lob
    @Column(name = "foto_perfil")
    private byte[] fotoPerfil;

    @OneToMany(mappedBy = "taller", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Builder.Default //si no lo inicializo como array se quedara en null por culpa de lmbmok
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
