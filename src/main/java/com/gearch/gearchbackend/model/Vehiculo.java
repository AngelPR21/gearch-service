package com.gearch.gearchbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Vehiculo")
public class Vehiculo {

    @Id
    @Column(name = "id_vehiculo")
    private Integer id;

    private String marca;
    private String modelo;
    private Integer ano;
    private String matricula;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    // getters y setters
}
