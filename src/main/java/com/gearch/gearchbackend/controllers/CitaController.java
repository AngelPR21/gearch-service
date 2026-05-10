package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.models.Cita;
import com.gearch.gearchbackend.services.CitaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Controlador para las operaciones de citas del cliente
@RestController
@RequestMapping("/api/citas")
@RequiredArgsConstructor
public class CitaController {

    private final CitaService citaService;

    // GET /api/citas/usuario/{usuarioId}
    // Devuelve todas las citas de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Cita>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(citaService.findByUsuario(usuarioId));
    }

    // GET /api/citas/usuario/{usuarioId}/proximas
    // Devuelve las citas futuras de un usuario ordenadas por fecha
    @GetMapping("/usuario/{usuarioId}/proximas")
    public ResponseEntity<List<Cita>> getProximasByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(citaService.findProximasByUsuario(usuarioId));
    }

    // GET /api/citas/taller/{tallerId}
    // Devuelve todas las citas de un taller
    @GetMapping("/taller/{tallerId}")
    public ResponseEntity<List<Cita>> getByTaller(@PathVariable Long tallerId) {
        return ResponseEntity.ok(citaService.findByTaller(tallerId));
    }

    // POST /api/citas?usuarioId=1&tallerId=1&servicioId=1&vehiculoId=1 (vehiculoId opcional)
    // Crea una nueva cita validando horario y duplicados
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

    // PATCH /api/citas/{id}/estado?estado=CANCELADA
    // Cambia el estado de una cita (usado por el cliente para cancelar)
    @PatchMapping("/{id}/estado")
    public ResponseEntity<?> updateEstado(@PathVariable Long id, @RequestParam String estado) {
        try {
            return ResponseEntity.ok(citaService.updateEstado(id, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/citas/{id}
    // Elimina una cita permanentemente
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
