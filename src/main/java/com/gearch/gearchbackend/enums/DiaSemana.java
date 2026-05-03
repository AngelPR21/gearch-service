package com.gearch.gearchbackend.enums;

import java.time.DayOfWeek;

public enum DiaSemana {
    LUNES,
    MARTES,
    MIERCOLES,
    JUEVES,
    VIERNES,
    SABADO,
    DOMINGO;

    //pasa de monday a lunes buscando en el enum de DiaSemana, convierte Monday = 0 el .ordinal
    public static DiaSemana desdeDayOfWeek(DayOfWeek diaSemana) {
        return DiaSemana.values()[diaSemana.ordinal()];
    }
}
