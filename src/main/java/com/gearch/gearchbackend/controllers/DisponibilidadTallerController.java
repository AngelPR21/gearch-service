package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.models.DisponibilidadTaller;
import com.gearch.gearchbackend.services.DisponibilidadTallerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
    // Devuelve el horario semanal completo del taller
    @GetMapping("/taller/{tallerId}")
    public ResponseEntity<List<DisponibilidadTaller>> getHorarioSemanal(@PathVariable Long tallerId) {
        return ResponseEntity.ok(disponibilidadService.getHorarioSemanal(tallerId));
    }

    // GET /api/disponibilidad/taller/{tallerId}/horas-libres?fecha=2025-06-10
    // Devuelve las horas libres para reservar en una fecha concreta
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
}
