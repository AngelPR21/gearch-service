package com.gearch.gearchbackend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Taller")
public class Taller {

    @Id
    @Column(name = "id_taller")
    private Integer id;

    private String nombre_taller;
    private String direccion;
    private String telefono_taller;

    @ManyToOne
    @JoinColumn(name = "id_propietario")
    private Propietario propietario;

    @ManyToMany
    @JoinTable(
            name = "Taller_Servicio",
            joinColumns = @JoinColumn(name = "id_taller"),
            inverseJoinColumns = @JoinColumn(name = "id_servicio")
    )
    private List<Servicio> servicios;

    // getters y setters
}
