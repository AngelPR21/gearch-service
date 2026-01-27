package com.gearch.gearchbackend.service;

import com.gearch.gearchbackend.model.Reserva;
import com.gearch.gearchbackend.repository.ReservaRepository;
import org.springframework.stereotype.Service;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;

    public ReservaService(ReservaRepository reservaRepository) {
        this.reservaRepository = reservaRepository;
    }

    public Reserva crearReserva(Reserva reserva) {
        // Aquí va la lógica para evitar reservas duplicadas
        return reservaRepository.save(reserva);
    }
}
