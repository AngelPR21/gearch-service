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

    public List<DisponibilidadTaller> getHorarioSemanal(Long tallerId) {
        return disponibilidadRepository.findByTallerId(tallerId);
    }

    public List<LocalTime> getHorasDisponibles(Long tallerId, LocalDate fecha) {
        DiaSemana diaSemana = DiaSemana.desdeDayOfWeek(fecha.getDayOfWeek());

        DisponibilidadTaller horario =
                disponibilidadRepository.findByTallerIdAndDiaSemana(tallerId, diaSemana);

        if (horario == null) {
            return new ArrayList<>();
        }
       // Generar todos los slots del día
        List<LocalTime> todosLosSlots = new ArrayList<>();
        LocalTime slot = horario.getHoraInicio();
        while (slot.isBefore(horario.getHoraFin())) {
            todosLosSlots.add(slot);
            slot = slot.plusMinutes(horario.getIntervaloMinutos());
        }

        // Obtener horas ya ocupadas ese dia
        //Esto da las citas de principio a fin del dia
        LocalDateTime inicioDia = fecha.atStartOfDay();
        LocalDateTime finDia    = fecha.atTime(23, 59, 59);
        List<Cita> citasDelDia  = citaRepository
                .findByTallerIdAndFechaHoraBetween(tallerId, inicioDia, finDia);

        //saca la hora de la cita
        List<LocalTime> horasOcupadas = new ArrayList<>();
        for (Cita cita : citasDelDia) {
            horasOcupadas.add(cita.getFechaHora().toLocalTime());
        }

        // Devolver solo los slots libres
        List<LocalTime> horasLibres = new ArrayList<>();
        //comprueba la hora de todas las horas con la hora de las citas para sacar solo los libres
        for (LocalTime s : todosLosSlots) {
            if (!horasOcupadas.contains(s)) {
                horasLibres.add(s);
            }
        }
        return horasLibres;
    }
}
