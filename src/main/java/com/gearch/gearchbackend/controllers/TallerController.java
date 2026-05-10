package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.models.Taller;
import com.gearch.gearchbackend.services.TallerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

// Controlador publico de talleres, usado por el cliente para buscar y ver talleres
@RestController
@RequestMapping("/api/talleres")
@RequiredArgsConstructor
public class TallerController {

    private final TallerService tallerService;

    // GET /api/talleres
    // Devuelve todos los talleres
    @GetMapping
    public ResponseEntity<List<Taller>> getAll() {
        return ResponseEntity.ok(tallerService.findAll());
    }

    // GET /api/talleres/{id}
    // Devuelve un taller por id (usado en DetalleTaller)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(tallerService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/talleres/buscar?nombre=AutoTop
    // Busca talleres por nombre, sin distinguir mayusculas
    @GetMapping("/buscar")
    public ResponseEntity<List<Taller>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(tallerService.buscarPorNombre(nombre));
    }

    // GET /api/talleres/cercanos?lat=39.47&lng=-0.37&radio=10
    // Devuelve los talleres dentro del radio indicado en km, ordenados por distancia
    @GetMapping("/cercanos")
    public ResponseEntity<List<Taller>> buscarCercanos(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radio) {
        return ResponseEntity.ok(tallerService.buscarCercanos(lat, lng, radio));
    }
}
