package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.entities.Cita;
import com.gearch.gearchbackend.entities.DisponibilidadTaller;
import com.gearch.gearchbackend.entities.Taller;
import com.gearch.gearchbackend.enums.DiaSemana;
import com.gearch.gearchbackend.repositories.DisponibilidadTallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DisponibilidadTallerService {

    private final DisponibilidadTallerRepository disponibilidadRepository;
    private final TallerRepository tallerRepository;
    private final CitaRepository citaRepository;

    // ── Consultas públicas (cliente) ──────────────────────────────────────────

    /** Devuelve el horario semanal completo de un taller.
     *  Android lo usa para saber qué días están activos en el calendario. */
    public List<DisponibilidadTaller> getHorarioSemanal(Long tallerId) {
        return disponibilidadRepository.findByTallerId(tallerId);
    }

    /**
     * Devuelve las horas libres para reservar en una fecha concreta.
     *
     * Pasos:
     * 1. Convierte la fecha a DiaSemana (usando el enum en español)
     * 2. Busca si el taller tiene horario configurado ese día
     * 3. Genera todos los slots según el intervalo (ej: 09:00, 09:30, 10:00...)
     * 4. Quita los slots que ya tienen una cita reservada
     * 5. Devuelve solo las horas libres
     *
     * Ejemplo de respuesta: ["09:00", "09:30", "10:30", "11:00"]
     */
    public List<LocalTime> getHorasDisponibles(Long tallerId, LocalDate fecha) {

        // 1. Convertir fecha → DiaSemana usando el helper del enum
        DiaSemana diaSemana = DiaSemana.desdeDayOfWeek(fecha.getDayOfWeek());

        // 2. ¿Trabaja el taller ese día?
        Optional<DisponibilidadTaller> disponibilidad =
                disponibilidadRepository.findByTallerIdAndDiaSemana(tallerId, diaSemana);

        if (disponibilidad.isEmpty()) {
            return List.of(); // El taller no trabaja ese día
        }

        DisponibilidadTaller horario = disponibilidad.get();

        // 3. Generar todos los slots del día según el intervalo configurado
        List<LocalTime> todosLosSlots = new ArrayList<>();
        LocalTime slot = horario.getHoraInicio();
        while (slot.isBefore(horario.getHoraFin())) {
            todosLosSlots.add(slot);
            slot = slot.plusMinutes(horario.getIntervaloMinutos());
        }

        // 4. Obtener las citas ya reservadas ese día concreto
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia    = fecha.atTime(23, 59, 59);
        List<Cita> citasDelDia  = citaRepository
                .findByTallerIdAndFechaHoraBetween(tallerId, inicioDia, finDia);

        List<LocalTime> horasOcupadas = citasDelDia.stream()
                .map(c -> c.getFechaHora().toLocalTime())
                .toList();

        // 5. Filtrar: solo los slots que no están ocupados
        return todosLosSlots.stream()
                .filter(s -> !horasOcupadas.contains(s))
                .toList();
    }

    // ── Gestión (llamada desde AdminTallerService) ────────────────────────────

    /**
     * Guarda o actualiza el horario de un día concreto del taller.
     * Si el admin ya tenía configurado ese día, lo sobreescribe.
     */
    public DisponibilidadTaller guardarDisponibilidad(Long tallerId, DisponibilidadTaller disponibilidad) {
        Taller taller = tallerRepository.findById(tallerId)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado con id: " + tallerId));

        // Si ya existe ese día para ese taller, reutilizamos el mismo id para hacer UPDATE
        disponibilidadRepository
                .findByTallerIdAndDiaSemana(tallerId, disponibilidad.getDiaSemana())
                .ifPresent(d -> disponibilidad.setId(d.getId()));

        disponibilidad.setTaller(taller);
        return disponibilidadRepository.save(disponibilidad);
    }

    /** El admin elimina la disponibilidad de un día (taller cerrado ese día) */
    public void eliminarDisponibilidad(Long disponibilidadId) {
        if (!disponibilidadRepository.existsById(disponibilidadId)) {
            throw new RuntimeException("Disponibilidad no encontrada con id: " + disponibilidadId);
        }
        disponibilidadRepository.deleteById(disponibilidadId);
    }
}
