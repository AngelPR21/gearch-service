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

    public static DiaSemana desdeDayOfWeek(DayOfWeek dow) {
        return DiaSemana.values()[dow.ordinal()];
    }
}
