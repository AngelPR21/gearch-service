package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.models.Cita;
import com.gearch.gearchbackend.services.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

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

    // POST /api/citas?usuarioId=1&tallerId=1&servicioId=1&vehiculoId=1 (vehiculoId opcional)
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