package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.Cita;
import com.gearch.gearchbackend.models.DisponibilidadTaller;
import com.gearch.gearchbackend.enums.DiaSemana;
import com.gearch.gearchbackend.repositories.CitaRepository;
import com.gearch.gearchbackend.repositories.DisponibilidadTallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DisponibilidadTallerService {

    private final DisponibilidadTallerRepository disponibilidadRepository;
    private final CitaRepository citaRepository;

    // Devuelve el horario semanal completo de un taller (todos sus dias)
    public List<DisponibilidadTaller> getHorarioSemanal(Long tallerId) {
        return disponibilidadRepository.findByTallerId(tallerId);
    }

    // Calcula las horas libres de un taller para una fecha concreta
    // Genera todos los slots del dia segun el horario y descarta los que ya tienen cita
    public List<LocalTime> getHorasDisponibles(Long tallerId, LocalDate fecha) {
        DiaSemana diaSemana = DiaSemana.desdeDayOfWeek(fecha.getDayOfWeek());

        DisponibilidadTaller horario =
                disponibilidadRepository.findByTallerIdAndDiaSemana(tallerId, diaSemana);

        // Si el taller no trabaja ese dia devuelve lista vacia
        if (horario == null) {
            return new ArrayList<>();
        }

        // Genera todos los slots del dia segun hora inicio, hora fin e intervalo
        List<LocalTime> todosLosSlots = new ArrayList<>();
        LocalTime slot = horario.getHoraInicio();
        while (slot.isBefore(horario.getHoraFin())) {
            todosLosSlots.add(slot);
            slot = slot.plusMinutes(horario.getIntervaloMinutos());
        }

        // Obtiene todas las citas del taller ese dia (de 00:00:00 a 23:59:59)
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia    = fecha.atTime(23, 59, 59);
        List<Cita> citasDelDia  = citaRepository
                .findByTallerIdAndFechaHoraBetween(tallerId, inicioDia, finDia);

        // Extrae solo la hora de cada cita para comparar con los slots
        List<LocalTime> horasOcupadas = new ArrayList<>();
        for (Cita cita : citasDelDia) {
            horasOcupadas.add(cita.getFechaHora().toLocalTime());
        }

        // Devuelve solo los slots que no tienen cita asignada
        List<LocalTime> horasLibres = new ArrayList<>();
        for (LocalTime s : todosLosSlots) {
            if (!horasOcupadas.contains(s)) {
                horasLibres.add(s);
            }
        }
        return horasLibres;
    }
}
