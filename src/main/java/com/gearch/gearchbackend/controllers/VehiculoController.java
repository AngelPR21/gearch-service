package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.models.Vehiculo;
import com.gearch.gearchbackend.services.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Controlador para las operaciones de vehiculos del usuario
@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    // GET /api/vehiculos/usuario/{usuarioId}
    // Devuelve todos los vehiculos de un usuario
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Vehiculo>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(vehiculoService.findByUsuario(usuarioId));
    }

    // POST /api/vehiculos/usuario/{usuarioId}
    // Crea un nuevo vehiculo para el usuario
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> create(@PathVariable Long usuarioId, @RequestBody Vehiculo vehiculo) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(vehiculoService.save(usuarioId, vehiculo));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/vehiculos/{id}
    // Actualiza los datos de un vehiculo
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Vehiculo vehiculo) {
        try {
            return ResponseEntity.ok(vehiculoService.update(id, vehiculo));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/vehiculos/{id}
    // Elimina un vehiculo por id
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            vehiculoService.delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Vehiculo eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }
}
