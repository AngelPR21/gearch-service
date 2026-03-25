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

    /**
     * Convierte el DayOfWeek de Java (en inglés) a nuestro enum DiaSemana (en español).
     * Se usa al recibir una fecha del cliente y buscar su disponibilidad.
     */
    public static DiaSemana desdeDayOfWeek(DayOfWeek dow) {
        return switch (dow) {
            case MONDAY    -> LUNES;
            case TUESDAY   -> MARTES;
            case WEDNESDAY -> MIERCOLES;
            case THURSDAY  -> JUEVES;
            case FRIDAY    -> VIERNES;
            case SATURDAY  -> SABADO;
            case SUNDAY    -> DOMINGO;
        };
    }
}
