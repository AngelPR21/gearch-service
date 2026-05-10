package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.models.Servicio;
import com.gearch.gearchbackend.services.ServicioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Controlador publico de servicios, usado por el cliente para ver los servicios de un taller
@RestController
@RequestMapping("/api/servicios")
@RequiredArgsConstructor
public class ServicioController {

    private final ServicioService servicioService;

    // GET /api/servicios/taller/{tallerId}
    // Devuelve todos los servicios de un taller
    @GetMapping("/taller/{tallerId}")
    public ResponseEntity<List<Servicio>> getByTaller(@PathVariable Long tallerId) {
        return ResponseEntity.ok(servicioService.findByTaller(tallerId));
    }
}
