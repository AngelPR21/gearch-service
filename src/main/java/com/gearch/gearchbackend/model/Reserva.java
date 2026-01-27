package com.gearch.gearchbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "Reserva")
public class Reserva {

    @Id
    @Column(name = "id_reserva")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "id_taller")
    private Taller taller;

    @ManyToOne
    @JoinColumn(name = "id_servicio")
    private Servicio servicio;

    @ManyToOne
    @JoinColumn(name = "id_calendario")
    private Calendario calendario;

    @ManyToOne(optional = true)
    @JoinColumn(name = "id_vehiculo")
    private Vehiculo vehiculo;

    // getters y setters
}
