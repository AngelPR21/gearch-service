package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.*;
import com.gearch.gearchbackend.enums.EstadoCita;
import com.gearch.gearchbackend.enums.RolUsuario;
import com.gearch.gearchbackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

// Service exclusivo para las operaciones del panel de administracion del taller
// Todos los metodos verifican que el usuario sea ADMIN_TALLER y que tenga un taller asignado
@Service
@RequiredArgsConstructor
public class AdminTallerService {

    private final UsuarioRepository              usuarioRepository;
    private final TallerRepository               tallerRepository;
    private final ServicioRepository             servicioRepository;
    private final CitaRepository                 citaRepository;
    private final ResenaRepository               resenaRepository;
    private final DisponibilidadTallerRepository disponibilidadRepository;

    // Verifica que el usuario existe, tiene rol ADMIN_TALLER y tiene un taller asignado
    // Se llama al inicio de cada metodo para proteger todas las operaciones
    private Usuario verificarAdmin(Long adminId) {
        Usuario admin = usuarioRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + adminId));
        if (admin.getRol() != RolUsuario.ADMIN_TALLER) {
            throw new RuntimeException("El usuario no tiene rol ADMIN_TALLER.");
        }
        if (admin.getTallerAdministrado() == null) {
            throw new RuntimeException("El admin no tiene ningun taller asignado.");
        }
        return admin;
    }

    // Devuelve el taller que administra el admin
    public Taller getMiTaller(Long adminId) {
        return verificarAdmin(adminId).getTallerAdministrado();
    }

    // Actualiza los datos del taller del admin
    public Taller actualizarMiTaller(Long adminId, Taller datos) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        taller.setNombre(datos.getNombre());
        taller.setDireccion(datos.getDireccion());
        taller.setTelefono(datos.getTelefono());
        taller.setDescripcion(datos.getDescripcion());
        taller.setLatitud(datos.getLatitud());
        taller.setLongitud(datos.getLongitud());
        return tallerRepository.save(taller);
    }

    // Devuelve el horario semanal del taller del admin
    public List<DisponibilidadTaller> getMiHorario(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return disponibilidadRepository.findByTallerId(taller.getId());
    }

    // Crea o actualiza el horario de un dia concreto
    // Si ya existe un horario para ese dia lo sobreescribe poniendo el mismo id
    public DisponibilidadTaller guardarDiaHorario(Long adminId, DisponibilidadTaller disponibilidad) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        DisponibilidadTaller existente = disponibilidadRepository
                .findByTallerIdAndDiaSemana(taller.getId(), disponibilidad.getDiaSemana());
        if (existente != null) {
            // Al poner el mismo id JPA hace un UPDATE en vez de un INSERT
            disponibilidad.setId(existente.getId());
        }
        disponibilidad.setTaller(taller);
        return disponibilidadRepository.save(disponibilidad);
    }

    // Elimina el horario de un dia verificando que pertenezca al taller del admin
    public void eliminarDiaHorario(Long adminId, Long disponibilidadId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        DisponibilidadTaller d = disponibilidadRepository.findById(disponibilidadId)
                .orElseThrow(() -> new RuntimeException("Horario no encontrado con id: " + disponibilidadId));
        if (!d.getTaller().getId().equals(taller.getId())) {
            throw new RuntimeException("Ese horario no pertenece a tu taller.");
        }
        disponibilidadRepository.deleteById(disponibilidadId);
    }

    // Devuelve los servicios del taller del admin
    public List<Servicio> getMisServicios(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return servicioRepository.findByTallerId(taller.getId());
    }

    // Crea un nuevo servicio y lo asigna al taller del admin
    public Servicio agregarServicio(Long adminId, Servicio servicio) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        servicio.setTaller(taller);
        return servicioRepository.save(servicio);
    }

    // Actualiza los datos de un servicio verificando que pertenezca al taller del admin
    public Servicio actualizarServicio(Long adminId, Long servicioId, Servicio datos) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + servicioId));
        if (!servicio.getTaller().getId().equals(taller.getId())) {
            throw new RuntimeException("Ese servicio no pertenece a tu taller.");
        }
        servicio.setNombre(datos.getNombre());
        servicio.setDescripcion(datos.getDescripcion());
        servicio.setPrecio(datos.getPrecio());
        return servicioRepository.save(servicio);
    }

    // Elimina un servicio verificando que pertenezca al taller del admin
    public void eliminarServicio(Long adminId, Long servicioId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + servicioId));
        if (!servicio.getTaller().getId().equals(taller.getId())) {
            throw new RuntimeException("Ese servicio no pertenece a tu taller.");
        }
        servicioRepository.deleteById(servicioId);
    }

    // Devuelve todas las citas del taller del admin
    public List<Cita> getMisCitas(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return citaRepository.findByTallerId(taller.getId());
    }

    // Devuelve las citas del taller del admin filtradas por estado
    public List<Cita> getMisCitasPorEstado(Long adminId, String estado) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return citaRepository.findByTallerIdAndEstado(
                taller.getId(), EstadoCita.valueOf(estado.toUpperCase()));
    }

    // Cambia el estado de una cita verificando que pertenezca al taller del admin
    public Cita cambiarEstadoCita(Long adminId, Long citaId, String nuevoEstado) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        Cita cita = citaRepository.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + citaId));
        if (!cita.getTaller().getId().equals(taller.getId())) {
            throw new RuntimeException("Esa cita no pertenece a tu taller.");
        }
        cita.setEstado(EstadoCita.valueOf(nuevoEstado.toUpperCase()));
        return citaRepository.save(cita);
    }

    // Devuelve todas las resenas del taller del admin
    public List<Resena> getMisResenas(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
        return resenaRepository.findByTallerId(taller.getId());
    }

    // Calcula estadisticas de las resenas: total y media de puntuacion
    public Map<String, Object> getEstadisticasResenas(Long adminId) {
        Taller taller = verificarAdmin(adminId).getTallerAdministrado();
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
