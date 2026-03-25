package com.gearch.gearchbackend.services;


import com.gearch.gearchbackend.entities.*;
import com.gearch.gearchbackend.enums.EstadoCita;
import com.gearch.gearchbackend.enums.RolUsuario;
import com.gearch.gearchbackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Servicio exclusivo para el panel de administración del taller.
 * Todas las operaciones comprueban que el usuarioId sea realmente
 * ADMIN_TALLER y que el taller que intenta gestionar sea el suyo.
 */
@Service
@RequiredArgsConstructor
public class AdminTallerService {

    private final UsuarioRepository usuarioRepository;
    private final TallerRepository tallerRepository;
    private final ServicioRepository servicioRepository;
    private final CitaRepository citaRepository;
    private final ResenaRepository              resenaRepository;
    private final DisponibilidadTallerRepository disponibilidadRepository;

    // ─────────────────────────────────────────────────────────────
    // Helpers privados
    // ─────────────────────────────────────────────────────────────

    /** Recupera el usuario y verifica que sea ADMIN_TALLER */
    private Usuario verificarAdmin(Long adminId) {
        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + adminId));
        if (admin.getRol() != RolUsuario.ADMIN_TALLER) {
            throw new RuntimeException("El usuario con id " + adminId + " no tiene rol ADMIN_TALLER.");
        }
        if (admin.getTallerAdministrado() == null) {
            throw new RuntimeException("El admin no tiene ningún taller asignado.");
        }
        return admin;
    }

    // ─────────────────────────────────────────────────────────────
    // Perfil del taller propio
    // ─────────────────────────────────────────────────────────────

    /** Devuelve el taller que administra este usuario */
    public Taller getMiTaller(Long adminId) {
        return verificarAdmin(adminId).getTallerAdministrado();
    }

    /** Actualiza los datos básicos del taller (nombre, dirección, teléfono...) */
    public Taller actualizarMiTaller(Long adminId, Taller datos) {
        Usuario admin  = verificarAdmin(adminId);
        Taller  taller = admin.getTallerAdministrado();
        taller.setNombre(datos.getNombre());
        taller.setDireccion(datos.getDireccion());
        taller.setTelefono(datos.getTelefono());
        taller.setDescripcion(datos.getDescripcion());
        taller.setLatitud(datos.getLatitud());
        taller.setLongitud(datos.getLongitud());
        return tallerRepository.save(taller);
    }

    // ─────────────────────────────────────────────────────────────
    // Gestión del horario semanal
    // ─────────────────────────────────────────────────────────────

    /** Devuelve todos los días configurados del horario del taller */
    public List<DisponibilidadTaller> getMiHorario(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return disponibilidadRepository.findByTallerId(taller.getId());
    }

    /**
     * Guarda o actualiza el horario de un día concreto.
     * Si ese día ya estaba configurado, lo sobreescribe (no duplica).
     *
     * Body esperado:
     * { "diaSemana": "LUNES", "horaInicio": "08:30", "horaFin": "18:00", "intervaloMinutos": 30 }
     */
    public DisponibilidadTaller guardarDiaHorario(Long adminId, DisponibilidadTaller disponibilidad) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();

        // Si ya existe ese día para este taller, reutiliza el mismo id → UPDATE en vez de INSERT
        disponibilidadRepository
                .findByTallerIdAndDiaSemana(taller.getId(), disponibilidad.getDiaSemana())
                .ifPresent(d -> disponibilidad.setId(d.getId()));

        disponibilidad.setTaller(taller);
        return disponibilidadRepository.save(disponibilidad);
    }

    /**
     * Elimina el horario de un día concreto del taller.
     * Equivale a marcar ese día como "cerrado".
     */
    public void eliminarDiaHorario(Long adminId, Long disponibilidadId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        DisponibilidadTaller d = disponibilidadRepository.findById(disponibilidadId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con id: " + disponibilidadId));
        // Verifica que ese día pertenece al taller del admin
        if (!d.getTaller().getId().equals(taller.getId())) {
            throw new RuntimeException("Ese horario no pertenece a tu taller.");
        }
        disponibilidadRepository.deleteById(disponibilidadId);
    }

    // ─────────────────────────────────────────────────────────────
    // Gestión de servicios
    // ─────────────────────────────────────────────────────────────

    public List<Servicio> getMisServicios(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return servicioRepository.findByTallerId(taller.getId());
    }

    public Servicio agregarServicio(Long adminId, Servicio servicio) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        servicio.setTaller(taller);
        return servicioRepository.save(servicio);
    }

    public Servicio actualizarServicio(Long adminId, Long servicioId, Servicio datos) {
        Usuario  admin    = verificarAdmin(adminId);
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + servicioId));
        if (!servicio.getTaller().getId().equals(admin.getTallerAdministrado().getId())) {
            throw new RuntimeException("Ese servicio no pertenece a tu taller.");
        }
        servicio.setNombre(datos.getNombre());
        servicio.setDescripcion(datos.getDescripcion());
        servicio.setPrecio(datos.getPrecio());
        servicio.setDuracionMinutos(datos.getDuracionMinutos());
        servicio.setTipo(datos.getTipo());
        return servicioRepository.save(servicio);
    }

    public void eliminarServicio(Long adminId, Long servicioId) {
        Usuario  admin    = verificarAdmin(adminId);
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + servicioId));
        if (!servicio.getTaller().getId().equals(admin.getTallerAdministrado().getId())) {
            throw new RuntimeException("Ese servicio no pertenece a tu taller.");
        }
        servicioRepository.deleteById(servicioId);
    }

    // ─────────────────────────────────────────────────────────────
    // Gestión de citas
    // ─────────────────────────────────────────────────────────────

    public List<Cita> getMisCitas(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return citaRepository.findByTallerId(taller.getId());
    }

    public List<Cita> getMisCitasPorEstado(Long adminId, String estado) {
        Taller     taller      = verificarAdmin(adminId).getTallerAdministrado();
        EstadoCita estadoCita  = EstadoCita.valueOf(estado.toUpperCase());
        return citaRepository.findByTallerIdAndEstado(taller.getId(), estadoCita);
    }

    public Cita cambiarEstadoCita(Long adminId, Long citaId, String nuevoEstado) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        Cita   cita   = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + citaId));
        if (!cita.getTaller().getId().equals(taller.getId())) {
            throw new RuntimeException("Esa cita no pertenece a tu taller.");
        }
        cita.setEstado(EstadoCita.valueOf(nuevoEstado.toUpperCase()));
        return citaRepository.save(cita);
    }

    // ─────────────────────────────────────────────────────────────
    // Reseñas (solo lectura)
    // ─────────────────────────────────────────────────────────────

    public List<Resena> getMisResenas(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return resenaRepository.findByTallerId(taller.getId());
    }

    public Map<String, Object> getEstadisticasResenas(Long adminId) {
        Taller       taller  = verificarAdmin(adminId).getTallerAdministrado();
        List<Resena> resenas = resenaRepository.findByTallerId(taller.getId());
        double media = resenas.stream()
                .mapToInt(Resena::getPuntuacion)
                .average()
                .orElse(0.0);
        return Map.of(
                "tallerId",        taller.getId(),
                "totalResenas",    resenas.size(),
                "puntuacionMedia", Math.round(media * 10.0) / 10.0
        );
    }
}
