package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.models.*;
import com.gearch.gearchbackend.services.AdminTallerService;
import com.gearch.gearchbackend.services.DisponibilidadTallerService;
import com.gearch.gearchbackend.services.TallerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

//CONTROLADOR PARA GESTIONAR TALLER
//http://localhost:8080/swagger-ui.html
/**
 * Panel de administración del tallere.
 * Base URL: /api/admin/{adminId}
 *
 * Todos los endpoints verifican que el adminId tenga rol ADMIN_TALLER
 * y que solo gestione su propio taller.
 */
@RestController
//TO-DO NECESITA EL ADMINID
@RequestMapping("/api/admin/{adminId}")
@RequiredArgsConstructor
public class AdminTallerController {

    private final AdminTallerService adminTallerService;
    private final TallerService tallerService;

    //Mi taller

    @GetMapping("/taller")
    public ResponseEntity<?> getMiTaller(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMiTaller(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

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

    //Horario semanal

    @GetMapping("/horario")
    public ResponseEntity<?> getMiHorario(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMiHorario(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/admin/{adminId}/horario
    // Crea o actualiza el horario de un día (si ya existe ese día lo sobreescribe)
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
    @DeleteMapping("/horario/{disponibilidadId}")
    public ResponseEntity<?> eliminarDiaHorario(
            @PathVariable Long adminId,
            @PathVariable Long disponibilidadId) {
        try {
            adminTallerService.eliminarDiaHorario(adminId, disponibilidadId);
            return ResponseEntity.ok(Map.of("mensaje", "Día eliminado del horario correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    //Servicios

    // GET /api/admin/{adminId}/servicios
    @GetMapping("/servicios")
    public ResponseEntity<?> getMisServicios(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMisServicios(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/admin/{adminId}/servicios
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

    //Citas

    @GetMapping("/citas")
    public ResponseEntity<?> getMisCitas(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMisCitas(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/admin/{adminId}/citas/estado?estado=PENDIENTE
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

    // PATCH /api/admin/{adminId}/citas/{citaId}/estado?estado=CONFIRMADA
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

    //Reseñas

    // GET /api/admin/{adminId}/resenas
    @GetMapping("/resenas")
    public ResponseEntity<?> getMisResenas(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMisResenas(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }


    @GetMapping("/resenas/estadisticas")
    public ResponseEntity<?> getEstadisticasResenas(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getEstadisticasResenas(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    //Foto de perfil

    /**
     * POST /api/admin/{adminId}/taller/foto
     * Sube o reemplaza la foto de perfil del taller.
     * Content-Type: multipart/form-data
     * Campo: "foto" (JPEG, PNG, WEBP o GIF, máx 5 MB)
     *
     * Respuesta 200: { "urlFoto": "http://localhost:8080/uploads/talleres/taller-1-uuid.jpg" }
     */
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

    @DeleteMapping("/taller/foto")
    public ResponseEntity<?> eliminarFotoPerfil(@PathVariable Long adminId) {
        try {
            Taller taller = adminTallerService.getMiTaller(adminId);
            tallerService.actualizarFotoPerfil(taller.getId(), null); //Cuando sea null el frontend pondra la por defecto
            return ResponseEntity.ok(Map.of("mensaje", "Foto de perfil eliminada correctamente"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
