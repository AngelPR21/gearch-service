package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.Servicio;
import com.gearch.gearchbackend.repositories.ServicioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;

    //  Todos los servicios de un taller
    public List<Servicio> findByTaller(Long tallerId) {
        return servicioRepository.findByTallerId(tallerId);
    }
}
