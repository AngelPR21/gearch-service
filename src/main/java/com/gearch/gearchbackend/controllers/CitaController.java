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
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(citaService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/citas/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Cita>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(citaService.findByUsuario(usuarioId));
    }

    // GET /api/citas/usuario/{usuarioId}/proximas
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
    @GetMapping("/taller/{tallerId}/calendario")
    public ResponseEntity<List<Cita>> getCalendario(
            @PathVariable Long tallerId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return ResponseEntity.ok(citaService.findByTallerEnRango(tallerId, inicio, fin));
    }

    // POST /api/citas?usuarioId=1&tallerId=1&servicioId=1&vehiculoId=1 (vehiculoId opcional)
    // Body: { "fechaHora": "2025-07-10T10:00:00", "notas": "..." }
    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam Long usuarioId,
            @RequestParam Long tallerId,
            @RequestParam Long servicioId,
            @RequestParam(required = false) Long vehiculoId,
            @RequestBody Cita cita) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(citaService.save(usuarioId, tallerId, servicioId, vehiculoId, cita));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PATCH /api/citas/{id}/estado?estado=CONFIRMADA
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
