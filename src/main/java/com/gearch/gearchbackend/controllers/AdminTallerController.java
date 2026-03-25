package com.gearch.gearchbackend.controllers;


import com.gearch.gearchbackend.entities.DisponibilidadTaller;
import com.gearch.gearchbackend.entities.Servicio;
import com.gearch.gearchbackend.entities.Taller;
import com.gearch.gearchbackend.services.AdminTallerService;
import com.gearch.gearchbackend.services.DisponibilidadTallerService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

/**
 * Panel de administración del taller.
 * Base URL: /api/admin/{adminId}
 *
 * El servicio verifica en cada llamada que:
 *   1. El adminId tenga rol ADMIN_TALLER
 *   2. El recurso que gestiona pertenezca a su propio taller
 */
@RestController
@RequestMapping("/api/admin/{adminId}")
@RequiredArgsConstructor
public class AdminTallerController {

    private final AdminTallerService adminTallerService;
    private final DisponibilidadTallerService disponibilidadService;

    // ─────────────────────────────────────────────────────────────
    // Mi taller — datos básicos
    // ─────────────────────────────────────────────────────────────

    // GET /api/admin/{adminId}/taller
    // El admin consulta los datos de su taller
    @GetMapping("/taller")
    public ResponseEntity<?> getMiTaller(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMiTaller(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/admin/{adminId}/taller
    // El admin actualiza nombre, dirección, teléfono, descripción y coordenadas
    // Body: { "nombre":"...", "direccion":"...", "telefono":"...", "descripcion":"...", "latitud":0.0, "longitud":0.0 }
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

    // ─────────────────────────────────────────────────────────────
    // Horario semanal — configuración de disponibilidad
    // ─────────────────────────────────────────────────────────────

    // GET /api/admin/{adminId}/horario
    // Devuelve todos los días configurados del taller (pantalla "Configurar horario")
    // Ejemplo de respuesta:
    // [ { "id":1, "diaSemana":"LUNES",   "horaInicio":"08:30", "horaFin":"18:00", "intervaloMinutos":30 },
    //   { "id":2, "diaSemana":"SABADO",  "horaInicio":"09:00", "horaFin":"13:00", "intervaloMinutos":30 } ]
    @GetMapping("/horario")
    public ResponseEntity<?> getMiHorario(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMiHorario(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/admin/{adminId}/horario
    // El admin configura o actualiza el horario de un día de la semana.
    // Si ese día ya estaba configurado, lo sobreescribe (no crea duplicado).
    //
    // Body: { "diaSemana": "LUNES", "horaInicio": "08:30", "horaFin": "18:00", "intervaloMinutos": 30 }
    //
    // diaSemana: LUNES | MARTES | MIERCOLES | JUEVES | VIERNES | SABADO | DOMINGO
    // intervaloMinutos: cada cuántos minutos hay un slot disponible (30 recomendado)
    @PostMapping("/horario")
    public ResponseEntity<?> guardarDiaHorario(
            @PathVariable Long adminId,
            @RequestBody DisponibilidadTaller disponibilidad) {
        try {
            DisponibilidadTaller guardado = adminTallerService.guardarDiaHorario(adminId, disponibilidad);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/admin/{adminId}/horario/{disponibilidadId}
    // El admin elimina un día del horario (ese día queda como "cerrado")
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

    // ─────────────────────────────────────────────────────────────
    // Servicios del taller
    // ─────────────────────────────────────────────────────────────

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
    // Body: { "nombre":"...", "descripcion":"...", "precio":50.0, "duracionMinutos":30, "tipo":"CAMBIO_ACEITE" }
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

    // ─────────────────────────────────────────────────────────────
    // Citas del taller
    // ─────────────────────────────────────────────────────────────

    // GET /api/admin/{adminId}/citas
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

    // ─────────────────────────────────────────────────────────────
    // Reseñas del taller (solo lectura)
    // ─────────────────────────────────────────────────────────────

    // GET /api/admin/{adminId}/resenas
    @GetMapping("/resenas")
    public ResponseEntity<?> getMisResenas(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getMisResenas(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // GET /api/admin/{adminId}/resenas/estadisticas
    // Devuelve: { "tallerId":1, "totalResenas":12, "puntuacionMedia":4.3 }
    @GetMapping("/resenas/estadisticas")
    public ResponseEntity<?> getEstadisticasResenas(@PathVariable Long adminId) {
        try {
            return ResponseEntity.ok(adminTallerService.getEstadisticasResenas(adminId));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Horas libres (también accesible desde el panel del admin
    // para previsualizar cómo ve el calendario un cliente)
    // ─────────────────────────────────────────────────────────────

    // GET /api/admin/{adminId}/horas-libres?fecha=2025-06-10
    @GetMapping("/horas-libres")
    public ResponseEntity<?> getHorasLibres(
            @PathVariable Long adminId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha) {
        try {
            Taller taller = adminTallerService.getMiTaller(adminId);
            List<LocalTime> horas = disponibilidadService.getHorasDisponibles(taller.getId(), fecha);
            if (horas.isEmpty()) {
                return ResponseEntity.ok(Map.of(
                        "mensaje", "El taller no tiene disponibilidad para esa fecha",
                        "horas", List.of()
                ));
            }
            return ResponseEntity.ok(horas);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
