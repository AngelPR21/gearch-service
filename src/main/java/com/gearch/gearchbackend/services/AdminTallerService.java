package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.*;
import com.gearch.gearchbackend.enums.EstadoCita;
import com.gearch.gearchbackend.enums.RolUsuario;
import com.gearch.gearchbackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminTallerService {

    private final UsuarioRepository              usuarioRepository;
    private final TallerRepository               tallerRepository;
    private final ServicioRepository             servicioRepository;
    private final CitaRepository                 citaRepository;
    private final ResenaRepository               resenaRepository;
    private final DisponibilidadTallerRepository disponibilidadRepository;

    private Usuario verificarAdmin(Long adminId) {
        if (!usuarioRepository.existsById(adminId)) {
            throw new RuntimeException("Usuario no encontrado con id: " + adminId);
        }
        Usuario admin = usuarioRepository.findById(adminId).get();
        if (admin.getRol() != RolUsuario.ADMIN_TALLER) {
            throw new RuntimeException("El usuario no tiene rol ADMIN_TALLER.");
        }
        if (admin.getTallerAdministrado() == null) {
            throw new RuntimeException("El admin no tiene ningún taller asignado.");
        }
        return admin;
    }

    // ── Mi taller ─────────────────────────────────────────────────
    public Taller getMiTaller(Long adminId) {
        return verificarAdmin(adminId).getTallerAdministrado();
    }

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

    // ── Horario ───────────────────────────────────────────────────
    public List<DisponibilidadTaller> getMiHorario(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return disponibilidadRepository.findByTallerId(taller.getId());
    }

    public DisponibilidadTaller guardarDiaHorario(Long adminId, DisponibilidadTaller disponibilidad) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        DisponibilidadTaller existente = disponibilidadRepository
                .findByTallerIdAndDiaSemana(taller.getId(), disponibilidad.getDiaSemana());
        if (existente != null) {
            disponibilidad.setId(existente.getId());
        }
        disponibilidad.setTaller(taller);
        return disponibilidadRepository.save(disponibilidad);
    }

    public void eliminarDiaHorario(Long adminId, Long disponibilidadId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        if (!disponibilidadRepository.existsById(disponibilidadId)) {
            throw new RuntimeException("Horario no encontrado con id: " + disponibilidadId);
        }
        DisponibilidadTaller d = disponibilidadRepository.findById(disponibilidadId).get();
        if (!d.getTaller().getId().equals(taller.getId())) {
            throw new RuntimeException("Ese horario no pertenece a tu taller.");
        }
        disponibilidadRepository.deleteById(disponibilidadId);
    }

    // ── Servicios ─────────────────────────────────────────────────
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
        Usuario admin = verificarAdmin(adminId);
        if (!servicioRepository.existsById(servicioId)) {
            throw new RuntimeException("Servicio no encontrado con id: " + servicioId);
        }
        Servicio servicio = servicioRepository.findById(servicioId).get();
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
        Usuario admin = verificarAdmin(adminId);
        if (!servicioRepository.existsById(servicioId)) {
            throw new RuntimeException("Servicio no encontrado con id: " + servicioId);
        }
        Servicio servicio = servicioRepository.findById(servicioId).get();
        if (!servicio.getTaller().getId().equals(admin.getTallerAdministrado().getId())) {
            throw new RuntimeException("Ese servicio no pertenece a tu taller.");
        }
        servicioRepository.deleteById(servicioId);
    }

    // ── Citas ─────────────────────────────────────────────────────
    public List<Cita> getMisCitas(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return citaRepository.findByTallerId(taller.getId());
    }

    public List<Cita> getMisCitasPorEstado(Long adminId, String estado) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return citaRepository.findByTallerIdAndEstado(
                taller.getId(), EstadoCita.valueOf(estado.toUpperCase()));
    }

    public Cita cambiarEstadoCita(Long adminId, Long citaId, String nuevoEstado) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        if (!citaRepository.existsById(citaId)) {
            throw new RuntimeException("Cita no encontrada con id: " + citaId);
        }
        Cita cita = citaRepository.findById(citaId).get();
        if (!cita.getTaller().getId().equals(taller.getId())) {
            throw new RuntimeException("Esa cita no pertenece a tu taller.");
        }
        cita.setEstado(EstadoCita.valueOf(nuevoEstado.toUpperCase()));
        return citaRepository.save(cita);
    }

    // ── Reseñas ───────────────────────────────────────────────────
    public List<Resena> getMisResenas(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return resenaRepository.findByTallerId(taller.getId());
    }

    public Map<String, Object> getEstadisticasResenas(Long adminId) {
        Taller       taller  = verificarAdmin(adminId).getTallerAdministrado();
        List<Resena> resenas = resenaRepository.findByTallerId(taller.getId());
        double suma = 0;
        for (Resena r : resenas) {
            suma += r.getPuntuacion();
        }
        double media = resenas.isEmpty() ? 0.0 : Math.round((suma / resenas.size()) * 10.0) / 10.0;
        return Map.of(
                "tallerId",        taller.getId(),
                "totalResenas",    resenas.size(),
                "puntuacionMedia", media
        );
    }
}
