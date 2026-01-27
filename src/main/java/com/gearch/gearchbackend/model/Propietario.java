package com.gearch.gearchbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Propietario")
public class Propietario {
    @Id
    @Column(name = "id_propietario")
    private Integer id;

    private String nombre_propietario;
    private String email_propietario;
    private String telefono_propietario;
    private String contrasena_propietario;

    // getters y setters
}
