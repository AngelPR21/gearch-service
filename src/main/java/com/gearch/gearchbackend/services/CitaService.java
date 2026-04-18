package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.*;
import com.gearch.gearchbackend.enums.DiaSemana;
import com.gearch.gearchbackend.enums.EstadoCita;
import com.gearch.gearchbackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CitaService {

    private final CitaRepository citaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TallerRepository tallerRepository;
    private final ServicioRepository servicioRepository;
    private final VehiculoRepository vehiculoRepository;
    private final DisponibilidadTallerRepository disponibilidadRepository;

    public List<Cita> findAll() {
        return citaRepository.findAll();
    }

    public Cita findById(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new RuntimeException("Cita no encontrada con id: " + id);
        }
        return citaRepository.getReferenceById(id);
    }

    public List<Cita> findByUsuario(Long usuarioId) {
        return citaRepository.findByUsuarioId(usuarioId);
    }

    public List<Cita> findProximasByUsuario(Long usuarioId) {
        return citaRepository.findByUsuarioIdAndFechaHoraAfterOrderByFechaHoraAsc(
                usuarioId, LocalDateTime.now());
    }

    public List<Cita> findByTaller(Long tallerId) {
        return citaRepository.findByTallerId(tallerId);
    }

    public List<Cita> findByTallerEnRango(Long tallerId, LocalDateTime inicio, LocalDateTime fin) {
        return citaRepository.findByTallerIdAndFechaHoraBetween(tallerId, inicio, fin);
    }

    public Cita save(Long usuarioId, Long tallerId, Long servicioId,
                     Long vehiculoId, Cita cita) {

        // primero comprobar duplicado
        if (citaRepository.existsByTallerIdAndFechaHora(tallerId, cita.getFechaHora())) {
            throw new RuntimeException("Ya existe una cita en ese taller para esa fecha y hora.");
        }

        //validar que la hora este dentro del horario del taller
        LocalDate fecha = cita.getFechaHora().toLocalDate();
        LocalTime hora  = cita.getFechaHora().toLocalTime();
        DiaSemana diaSemana = DiaSemana.desdeDayOfWeek(fecha.getDayOfWeek());

        DisponibilidadTaller horario =
                disponibilidadRepository.findByTallerIdAndDiaSemana(tallerId, diaSemana);

        if (horario == null) {
            throw new RuntimeException("El taller no tiene disponibilidad ese día de la semana.");
        }
        if (hora.isBefore(horario.getHoraInicio()) || !hora.isBefore(horario.getHoraFin())) {
            throw new RuntimeException("La hora elegida está fuera del horario del taller ("
                    + horario.getHoraInicio() + " - " + horario.getHoraFin() + ").");
        }

        //buscar si existe el usuario, taller y servicio, si existe lo guarda en la cita si no tira excepcion
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con id: " + usuarioId);
        }
        if (!tallerRepository.existsById(tallerId)) {
            throw new RuntimeException("Taller no encontrado con id: " + tallerId);
        }
        if (!servicioRepository.existsById(servicioId)) {
            throw new RuntimeException("Servicio no encontrado con id: " + servicioId);
        }

        cita.setUsuario(usuarioRepository.getReferenceById(usuarioId));
        cita.setTaller(tallerRepository.getReferenceById(tallerId));
        cita.setServicio(servicioRepository.getReferenceById(servicioId));
        cita.setEstado(EstadoCita.PENDIENTE);

        //Vehiculo opcional
        if (vehiculoId != null) {
            if (!vehiculoRepository.existsById(vehiculoId)) {
                throw new RuntimeException("Vehículo no encontrado con id: " + vehiculoId);
            }
            cita.setVehiculo(vehiculoRepository.getReferenceById(vehiculoId));
        }

        return citaRepository.save(cita);
    }

    public Cita updateEstado(Long id, String estado) {
        if (!citaRepository.existsById(id)) {
            throw new RuntimeException("Cita no encontrada con id: " + id);
        }
        Cita cita = citaRepository.getReferenceById(id);
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
