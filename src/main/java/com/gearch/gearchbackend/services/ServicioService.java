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

    public List<Servicio> findAll() {
        return servicioRepository.findAll();
    }

    public Servicio findById(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado con id: " + id);
        }
        return servicioRepository.getReferenceById(id);
    }

    public List<Servicio> findByTaller(Long tallerId) {
        return servicioRepository.findByTallerId(tallerId);
    }
}
