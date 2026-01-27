package com.gearch.gearchbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Cliente")
public class Cliente {

    @Id
    @Column(name = "id_cliente")
    private Integer id;

    private String nombre_cliente;
    private String email_cliente;
    private String telefono_cliente;
    private String contrasena_cliente;

    // getters y setters
}
