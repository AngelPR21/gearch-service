package com.gearch.gearchbackend.controllers;


import com.gearch.gearchbackend.entities.Vehiculo;
import com.gearch.gearchbackend.services.VehiculoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vehiculos")
@RequiredArgsConstructor
public class VehiculoController {

    private final VehiculoService vehiculoService;

    // GET /api/vehiculos
    @GetMapping
    public ResponseEntity<List<Vehiculo>> getAll() {
        return ResponseEntity.ok(vehiculoService.findAll());
    }

    // GET /api/vehiculos/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Vehiculo> getById(@PathVariable Long id) {
        return vehiculoService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/vehiculos/usuario/{usuarioId}
    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Vehiculo>> getByUsuario(@PathVariable Long usuarioId) {
        return ResponseEntity.ok(vehiculoService.findByUsuario(usuarioId));
    }

    // POST /api/vehiculos/usuario/{usuarioId}
    @PostMapping("/usuario/{usuarioId}")
    public ResponseEntity<?> create(@PathVariable Long usuarioId, @RequestBody Vehiculo vehiculo) {
        try {
            Vehiculo nuevo = vehiculoService.save(usuarioId, vehiculo);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/vehiculos/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Vehiculo vehiculo) {
        try {
            return ResponseEntity.ok(vehiculoService.update(id, vehiculo));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/vehiculos/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            vehiculoService.delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Vehículo eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
