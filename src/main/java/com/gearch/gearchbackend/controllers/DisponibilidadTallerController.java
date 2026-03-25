package com.gearch.gearchbackend.controllers;


import com.gearch.gearchbackend.entities.DisponibilidadTaller;
import com.gearch.gearchbackend.services.DisponibilidadTallerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/disponibilidad")
@RequiredArgsConstructor
public class DisponibilidadTallerController {

    private final DisponibilidadTallerService disponibilidadService;

    // GET /api/disponibilidad/taller/{tallerId}
    // Devuelve el horario semanal completo del taller (qué días trabaja y en qué franja)
    // Android lo usa para saber qué días mostrar activos en el calendario
    @GetMapping("/taller/{tallerId}")
    public ResponseEntity<List<DisponibilidadTaller>> getHorarioSemanal(@PathVariable Long tallerId) {
        return ResponseEntity.ok(disponibilidadService.getHorarioSemanal(tallerId));
    }

    // GET /api/disponibilidad/taller/{tallerId}/horas-libres?fecha=2025-06-10
    // ★ Endpoint principal para Android ★
    // Devuelve la lista de horas libres para reservar en una fecha concreta.
    // Android muestra estas horas en el selector de hora al crear una cita.
    // Ejemplo de respuesta: ["09:00", "09:30", "10:30", "11:00", ...]
    @GetMapping("/taller/{tallerId}/horas-libres")
    public ResponseEntity<?> getHorasDisponibles(
            @PathVariable Long tallerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            List<LocalTime> horas = disponibilidadService.getHorasDisponibles(tallerId, fecha);
            if (horas.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                    "mensaje", "El taller no tiene disponibilidad para esa fecha",
                    "horas", List.of()
                ));
            }
            return ResponseEntity.ok(horas);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/disponibilidad/taller/{tallerId}
    // El admin configura el horario de un día de la semana
    // Body: { "diaSemana": "LUNES", "horaInicio": "08:30", "horaFin": "18:00", "intervaloMinutos": 30 }
    @PostMapping("/taller/{tallerId}")
    public ResponseEntity<?> guardarDisponibilidad(
            @PathVariable Long tallerId,
            @RequestBody DisponibilidadTaller disponibilidad) {
        try {
            DisponibilidadTaller guardada = disponibilidadService.guardarDisponibilidad(tallerId, disponibilidad);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardada);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/disponibilidad/{id}
    // El admin elimina la disponibilidad de un día (taller cerrado ese día)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminarDisponibilidad(@PathVariable Long id) {
        try {
            disponibilidadService.eliminarDisponibilidad(id);
            return ResponseEntity.ok(Map.of("mensaje", "Disponibilidad eliminada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
