package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.entities.Servicio;
import com.gearch.gearchbackend.services.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    // GET /api/servicios
    @GetMapping
    public ResponseEntity<List<Servicio>> getAll() {
        return ResponseEntity.ok(servicioService.findAll());
    }

    // GET /api/servicios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(servicioService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/servicios/taller/{tallerId}
    @GetMapping("/taller/{tallerId}")
    public ResponseEntity<List<Servicio>> getByTaller(@PathVariable Long tallerId) {
        return ResponseEntity.ok(servicioService.findByTaller(tallerId));
    }

    // POST /api/servicios/taller/{tallerId}
    @PostMapping("/taller/{tallerId}")
    public ResponseEntity<?> create(@PathVariable Long tallerId, @RequestBody Servicio servicio) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(servicioService.save(tallerId, servicio));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/servicios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Servicio servicio) {
        try {
            return ResponseEntity.ok(servicioService.update(id, servicio));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/servicios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            servicioService.delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Servicio eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
