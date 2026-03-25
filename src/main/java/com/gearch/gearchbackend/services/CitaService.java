package com.gearch.gearchbackend.services;


import com.gearch.gearchbackend.entities.*;
import com.gearch.gearchbackend.enums.EstadoCita;
import com.gearch.gearchbackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TallerRepository tallerRepository;
    private final ServicioRepository servicioRepository;
    private final VehiculoRepository vehiculoRepository;

    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    public Optional<Cita> findById(Long id) {
        return citaRepository.findById(id);
    }

    public List<Cita> findByUsuario(Long usuarioId) {
        return citaRepository.findByUsuarioId(usuarioId);
    }

    /** Citas futuras del usuario ordenadas por fecha (para la pantalla "Mis próximas citas") */
    public List<Cita> findProximasByUsuario(Long usuarioId) {
        return citaRepository
                .findByUsuarioIdAndFechaHoraAfterOrderByFechaHoraAsc(usuarioId, LocalDateTime.now());
    }

    public List<Cita> findByTaller(Long tallerId) {
        return citaRepository.findByTallerId(tallerId);
    }

    public List<Cita> findByTallerEnRango(Long tallerId, LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findByTallerIdAndFechaHoraBetween(tallerId, inicio, fin);
    }

    public Cita save(Long usuarioId, Long tallerId, Long servicioId, Long vehiculoId, Cita cita) {

        // 1. Comprobar que la fecha/hora no está ya ocupada en ese taller
        if (citaRepository.existsByTallerIdAndFechaHora(tallerId, cita.getFechaHora())) {
            throw new RuntimeException("Ya existe una cita en ese taller para la fecha y hora indicadas.");
        }

        // 2. Cargar entidades relacionadas
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));

        Taller taller = tallerRepository.findById(tallerId)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado con id: " + tallerId));

        Servicio servicio = servicioRepository.findById(servicioId)
                .orElseThrow(() -> new RuntimeException("Servicio no encontrado con id: " + servicioId));

        // 3. El vehículo es opcional
        if (vehiculoId != null) {
            Vehiculo vehiculo = vehiculoRepository.findById(vehiculoId)
                    .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + vehiculoId));
            cita.setVehiculo(vehiculo);
        }

        cita.setUsuario(usuario);
        cita.setTaller(taller);
        cita.setServicio(servicio);
        cita.setEstado(EstadoCita.PENDIENTE);

        return citaRepository.save(cita);
    }

    public Cita updateEstado(Long id, String estado) {
        Cita cita = citaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada con id: " + id));
        cita.setEstado(EstadoCita.valueOf(estado.toUpperCase()));
        return citaRepository.save(cita);
    }

    public void delete(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new RuntimeException("Cita no encontrada con id: " + id);
        }
        citaRepository.deleteById(id);
    }
}
