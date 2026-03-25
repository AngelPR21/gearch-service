package com.gearch.gearchbackend.controllers;


import com.gearch.gearchbackend.entities.Cita;
import com.gearch.gearchbackend.services.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    // GET /api/citas
    @GetMapping
    public ResponseEntity<List<Cita>> getAll() {
        return ResponseEntity.ok(citaService.findAll());
    }

    // GET /api/citas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Cita> getById(@PathVariable Long id) {
        return citaService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/citas/usuario/{usuarioId}
    // Todas las citas del usuario (historial completo)
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Cita>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(citaService.findByUsuario(usuarioId));
    }

    // GET /api/citas/usuario/{usuarioId}/proximas
    // Solo las citas futuras del usuario, ordenadas por fecha (para la pantalla principal del cliente)
    @GetMapping("/usuario/{usuarioId}/proximas")
    public ResponseEntity<List<Cita>> getProximasByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(citaService.findProximasByUsuario(usuarioId));
    }

    // GET /api/citas/taller/{tallerId}
    @GetMapping("/taller/{tallerId}")
    public ResponseEntity<List<Cita>> getByTaller(@PathVariable Long tallerId) {
        return ResponseEntity.ok(citaService.findByTaller(tallerId));
    }

    // GET /api/citas/taller/{tallerId}/calendario?inicio=2025-06-01T00:00:00&fin=2025-06-30T23:59:59
    // Devuelve las horas ya ocupadas del taller en ese rango (para bloquear el calendario en Android)
    @GetMapping("/taller/{tallerId}/calendario")
    public ResponseEntity<List<Cita>> getCalendario(
            @PathVariable Long tallerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(citaService.findByTallerEnRango(tallerId, inicio, fin));
    }

    // POST /api/citas?usuarioId=1&tallerId=1&servicioId=1&vehiculoId=1
    // vehiculoId es OPCIONAL (puede omitirse si el usuario no es propietario del vehículo)
    // Body: { "fechaHora": "2025-07-10T10:00:00", "notas": "..." }
    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam Long usuarioId,
            @RequestParam Long tallerId,
            @RequestParam Long servicioId,
            @RequestParam(required = false) Long vehiculoId,
            @RequestBody Cita cita) {
        try {
            Cita nueva = citaService.save(usuarioId, tallerId, servicioId, vehiculoId, cita);
            return ResponseEntity.status(HttpStatus.CREATED).body(nueva);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PATCH /api/citas/{id}/estado?estado=CANCELADA
    // El propio usuario puede cancelar su cita
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            return ResponseEntity.ok(citaService.updateEstado(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/citas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            citaService.delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Cita eliminada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
