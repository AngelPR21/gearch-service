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

    // Todas las citas de un usuario
    public List<Cita> findByUsuario(Long usuarioId) {
        return citaRepository.findByUsuarioId(usuarioId);
    }

    // Proximas citas de un usuario ordenadas por fecha
    public List<Cita> findProximasByUsuario(Long usuarioId) {
        return citaRepository.findByUsuarioIdAndFechaHoraAfterOrderByFechaHoraAsc(
                usuarioId, LocalDateTime.now());
    }

    // Todas las citas de un taller
    public List<Cita> findByTaller(Long tallerId) {
        return citaRepository.findByTallerId(tallerId);
    }

    // Crea una nueva cita validando disponibilidad y duplicados
    public Cita save(Long usuarioId, Long tallerId, Long servicioId,
                     Long vehiculoId, Cita cita) {

        // Comprueba que no exista ya una cita en ese taller a esa hora
        if (citaRepository.existsByTallerIdAndFechaHora(tallerId, cita.getFechaHora())) {
            throw new RuntimeException("Ya existe una cita en ese taller para esa fecha y hora.");
        }

        // Valida que la hora elegida este dentro del horario del taller ese dia
        LocalDate fecha = cita.getFechaHora().toLocalDate();
        LocalTime hora  = cita.getFechaHora().toLocalTime();
        DiaSemana diaSemana = DiaSemana.desdeDayOfWeek(fecha.getDayOfWeek());

        DisponibilidadTaller horario =
                disponibilidadRepository.findByTallerIdAndDiaSemana(tallerId, diaSemana);

        if (horario == null) {
            throw new RuntimeException("El taller no tiene disponibilidad ese dia de la semana.");
        }
        if (hora.isBefore(horario.getHoraInicio()) || !hora.isBefore(horario.getHoraFin())) {
            throw new RuntimeException("La hora elegida esta fuera del horario del taller ("
                    + horario.getHoraInicio() + " - " + horario.getHoraFin() + ").");
        }

        // Verifica que existan el usuario, taller y servicio antes de asignarlos
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con id: " + usuarioId);
        }
        if (!tallerRepository.existsById(tallerId)) {
            throw new RuntimeException("Taller no encontrado con id: " + tallerId);
        }
        if (!servicioRepository.existsById(servicioId)) {
            throw new RuntimeException("Servicio no encontrado con id: " + servicioId);
        }

        // getReferenceById devuelve un proxy sin hacer una query extra, mas eficiente que findById
        cita.setUsuario(usuarioRepository.getReferenceById(usuarioId));
        cita.setTaller(tallerRepository.getReferenceById(tallerId));
        cita.setServicio(servicioRepository.getReferenceById(servicioId));

        // El vehiculo es opcional
        if (vehiculoId != null) {
            if (!vehiculoRepository.existsById(vehiculoId)) {
                throw new RuntimeException("Vehiculo no encontrado con id: " + vehiculoId);
            }
            cita.setVehiculo(vehiculoRepository.getReferenceById(vehiculoId));
        }

        return citaRepository.save(cita);
    }

    // Cambia el estado de una cita (CONFIRMADA, CANCELADA o COMPLETADA)
    public Cita updateEstado(Long id, String estado) {
        if (!citaRepository.existsById(id)) {
            throw new RuntimeException("Cita no encontrada con id: " + id);
        }
        Cita cita = citaRepository.getReferenceById(id);
        cita.setEstado(EstadoCita.valueOf(estado.toUpperCase()));
        return citaRepository.save(cita);
    }

    // Elimina una cita por id
    public void delete(Long id) {
        if (!citaRepository.existsById(id)) {
            throw new RuntimeException("Cita no encontrada con id: " + id);
        }
        citaRepository.deleteById(id);
    }
}
