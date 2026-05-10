package com.gearch.gearchbackend.enums;

import java.time.DayOfWeek;

// Enum con los dias de la semana en espanol
public enum DiaSemana {
    LUNES,
    MARTES,
    MIERCOLES,
    JUEVES,
    VIERNES,
    SABADO,
    DOMINGO;

    // Convierte el DayOfWeek de Java (MONDAY, TUESDAY...) al enum DiaSemana (LUNES, MARTES...)
    // DayOfWeek.ordinal() devuelve 0 para MONDAY, 1 para TUESDAY, etc.
    // DiaSemana.values() devuelve el array [LUNES, MARTES...] en el mismo orden
    public static DiaSemana desdeDayOfWeek(DayOfWeek diaSemana) {
        return DiaSemana.values()[diaSemana.ordinal()];
    }
}
