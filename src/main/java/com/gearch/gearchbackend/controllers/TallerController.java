package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.entities.Taller;
import com.gearch.gearchbackend.services.TallerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/talleres")
@RequiredArgsConstructor
public class TallerController {

    private final TallerService tallerService;

    // GET /api/talleres
    @GetMapping
    public ResponseEntity<List<Taller>> getAll() {
        return ResponseEntity.ok(tallerService.findAll());
    }

    // GET /api/talleres/{id}
    @GetMapping("/{id}")
    public ResponseEntity<Taller> getById(@PathVariable Long id) {
        return tallerService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/talleres/buscar?nombre=AutoTop
    @GetMapping("/buscar")
    public ResponseEntity<List<Taller>> buscarPorNombre(@RequestParam String nombre) {
        return ResponseEntity.ok(tallerService.buscarPorNombre(nombre));
    }

    // GET /api/talleres/cercanos?lat=39.47&lng=-0.37&radio=10
    @GetMapping("/cercanos")
    public ResponseEntity<List<Taller>> buscarCercanos(
            @RequestParam double lat,
            @RequestParam double lng,
            @RequestParam(defaultValue = "10") double radio) {
        return ResponseEntity.ok(tallerService.buscarCercanos(lat, lng, radio));
    }

    // POST /api/talleres
    @PostMapping
    public ResponseEntity<Taller> create(@RequestBody Taller taller) {
        return ResponseEntity.status(HttpStatus.CREATED).body(tallerService.save(taller));
    }

    // PUT /api/talleres/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Taller taller) {
        try {
            return ResponseEntity.ok(tallerService.update(id, taller));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/talleres/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            tallerService.delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Taller eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
