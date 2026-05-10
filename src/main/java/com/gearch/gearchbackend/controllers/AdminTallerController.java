package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.models.*;
import com.gearch.gearchbackend.services.AdminTallerService;
import com.gearch.gearchbackend.services.TallerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

// Controlador exclusivo para el panel de administracion del taller
// Todos los endpoints verifican que el adminId tenga rol ADMIN_TALLER
// URL base: /api/admin/{adminId}
@RestController
@RequestMapping("/api/admin/{adminId}")
@RequiredArgsConstructor
public class AdminTallerController {

    private final AdminTallerService adminTallerService;
    private final TallerService tallerService;

    // GET /api/admin/{adminId}/taller
    // Devuelve los datos del taller que administra el admin
    @GetMapping("/taller")
    public ResponseEntity<?> getMiTaller(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMiTaller(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/admin/{adminId}/taller
    // Actualiza los datos del taller del admin
    @PutMapping("/taller")
    public ResponseEntity<?> actualizarMiTaller(
            @PathVariable Long adminId,
            @RequestBody Taller taller) {
        try {
            return ResponseEntity.ok(adminTallerService.actualizarMiTaller(adminId, taller));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/admin/{adminId}/horario
    // Devuelve el horario semanal del taller
    @GetMapping("/horario")
    public ResponseEntity<?> getMiHorario(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMiHorario(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/admin/{adminId}/horario
    // Crea o sobreescribe el horario de un dia de la semana
    @PostMapping("/horario")
    public ResponseEntity<?> guardarDiaHorario(
            @PathVariable Long adminId,
            @RequestBody DisponibilidadTaller disponibilidad) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(adminTallerService.guardarDiaHorario(adminId, disponibilidad));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/admin/{adminId}/horario/{disponibilidadId}
    // Elimina el horario de un dia concreto
    @DeleteMapping("/horario/{disponibilidadId}")
    public ResponseEntity<?> eliminarDiaHorario(
            @PathVariable Long adminId,
            @PathVariable Long disponibilidadId) {
        try {
            adminTallerService.eliminarDiaHorario(adminId, disponibilidadId);
            return ResponseEntity.ok(Map.of("mensaje", "Dia eliminado del horario correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/admin/{adminId}/servicios
    // Devuelve los servicios del taller del admin
    @GetMapping("/servicios")
    public ResponseEntity<?> getMisServicios(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMisServicios(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/admin/{adminId}/servicios
    // Crea un nuevo servicio en el taller del admin
    @PostMapping("/servicios")
    public ResponseEntity<?> agregarServicio(
            @PathVariable Long adminId,
            @RequestBody Servicio servicio) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(adminTallerService.agregarServicio(adminId, servicio));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/admin/{adminId}/servicios/{servicioId}
    // Actualiza un servicio del taller del admin
    @PutMapping("/servicios/{servicioId}")
    public ResponseEntity<?> actualizarServicio(
            @PathVariable Long adminId,
            @PathVariable Long servicioId,
            @RequestBody Servicio servicio) {
        try {
            return ResponseEntity.ok(adminTallerService.actualizarServicio(adminId, servicioId, servicio));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/admin/{adminId}/servicios/{servicioId}
    // Elimina un servicio del taller del admin
    @DeleteMapping("/servicios/{servicioId}")
    public ResponseEntity<?> eliminarServicio(
            @PathVariable Long adminId,
            @PathVariable Long servicioId) {
        try {
            adminTallerService.eliminarServicio(adminId, servicioId);
            return ResponseEntity.ok(Map.of("mensaje", "Servicio eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/admin/{adminId}/citas
    // Devuelve todas las citas del taller del admin
    @GetMapping("/citas")
    public ResponseEntity<?> getMisCitas(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMisCitas(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/admin/{adminId}/citas/estado?estado=CONFIRMADA
    // Devuelve las citas del taller filtradas por estado
    @GetMapping("/citas/estado")
    public ResponseEntity<?> getMisCitasPorEstado(
            @PathVariable Long adminId,
            @RequestParam String estado) {
        try {
            return ResponseEntity.ok(adminTallerService.getMisCitasPorEstado(adminId, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PATCH /api/admin/{adminId}/citas/{citaId}/estado?estado=COMPLETADA
    // Cambia el estado de una cita del taller del admin
    @PatchMapping("/citas/{citaId}/estado")
    public ResponseEntity<?> cambiarEstadoCita(
            @PathVariable Long adminId,
            @PathVariable Long citaId,
            @RequestParam String estado) {
        try {
            return ResponseEntity.ok(adminTallerService.cambiarEstadoCita(adminId, citaId, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/admin/{adminId}/resenas
    // Devuelve todas las resenas del taller del admin
    @GetMapping("/resenas")
    public ResponseEntity<?> getMisResenas(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMisResenas(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/admin/{adminId}/resenas/estadisticas
    // Devuelve el total de resenas y la media de puntuacion del taller
    @GetMapping("/resenas/estadisticas")
    public ResponseEntity<?> getEstadisticasResenas(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getEstadisticasResenas(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/admin/{adminId}/taller/foto
    // Sube o reemplaza la foto de perfil del taller
    // Content-Type: multipart/form-data, campo "foto" (JPEG, PNG, WEBP o GIF, max 5 MB)
    @PostMapping(value = "/taller/foto", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> subirFotoPerfil(
            @PathVariable Long adminId,
            @RequestParam("foto") MultipartFile foto) {
        try {
            Taller taller = adminTallerService.getMiTaller(adminId);
            tallerService.actualizarFotoPerfil(taller.getId(), foto);
            return ResponseEntity.ok(Map.of("mensaje", "Foto de perfil actualizada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/admin/{adminId}/taller/foto
    // Elimina la foto de perfil del taller (el frontend mostrara la imagen por defecto)
    @DeleteMapping("/taller/foto")
    public ResponseEntity<?> eliminarFotoPerfil(@PathVariable Long adminId) {
        try {
            Taller taller = adminTallerService.getMiTaller(adminId);
            tallerService.actualizarFotoPerfil(taller.getId(), null);
            return ResponseEntity.ok(Map.of("mensaje", "Foto de perfil eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
