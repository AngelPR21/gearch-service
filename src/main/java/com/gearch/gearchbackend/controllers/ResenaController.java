package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.entities.Resena;
import com.gearch.gearchbackend.services.ResenaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/resenas")
@RequiredArgsConstructor
public class ResenaController {

    private final ResenaService resenaService;

    // GET /api/resenas
    @GetMapping
    public ResponseEntity<List<Resena>> getAll() {
        return ResponseEntity.ok(resenaService.findAll());
    }

    // GET /api/resenas/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(resenaService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/resenas/taller/{tallerId}
    @GetMapping("/taller/{tallerId}")
    public ResponseEntity<List<Resena>> getByTaller(@PathVariable Long tallerId) {
        return ResponseEntity.ok(resenaService.findByTaller(tallerId));
    }

    // GET /api/resenas/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Resena>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(resenaService.findByUsuario(usuarioId));
    }

    // POST /api/resenas?usuarioId=1&tallerId=1
    // Body: { "comentario": "...", "puntuacion": 5 }
    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam Long usuarioId,
            @RequestParam Long tallerId,
            @RequestBody Resena resena) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(resenaService.save(usuarioId, tallerId, resena));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/resenas/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            resenaService.delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Reseña eliminada correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
