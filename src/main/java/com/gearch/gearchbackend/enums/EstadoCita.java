package com.gearch.gearchbackend.enums;

// Estados posibles de una cita
// Las citas se crean siempre como CONFIRMADA
// El cliente puede cancelarla y el admin puede cancelarla o completarla
public enum EstadoCita {
    CONFIRMADA,
    CANCELADA,
    COMPLETADA
}
