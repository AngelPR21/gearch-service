package com.gearch.gearchbackend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "Servicio")
public class Servicio {

    @Id
    @Column(name = "id_servicio")
    private Integer id;

    private String nombre_servicio;
    private String descripcion;

    @ManyToMany(mappedBy = "servicios")
    private List<Taller> talleres;

    // getters y setters
}
