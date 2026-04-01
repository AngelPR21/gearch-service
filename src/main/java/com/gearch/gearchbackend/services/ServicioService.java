package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.Servicio;
import com.gearch.gearchbackend.models.Taller;
import com.gearch.gearchbackend.repositories.ServicioRepository;
import com.gearch.gearchbackend.repositories.TallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ServicioService {

    private final ServicioRepository servicioRepository;
    private final TallerRepository tallerRepository;

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

    public Servicio save(Long tallerId, Servicio servicio) {
        if (!tallerRepository.existsById(tallerId)) {
            throw new RuntimeException("Taller no encontrado con id: " + tallerId);
        }
        Taller taller = tallerRepository.getReferenceById(tallerId);
        servicio.setTaller(taller);
        return servicioRepository.save(servicio);
    }

    public Servicio update(Long id, Servicio datos) {
        if (!servicioRepository.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado con id: " + id);
        }
        Servicio servicio = servicioRepository.getReferenceById(id);
        servicio.setNombre(datos.getNombre());
        servicio.setDescripcion(datos.getDescripcion());
        servicio.setPrecio(datos.getPrecio());
        servicio.setDuracionMinutos(datos.getDuracionMinutos());
        servicio.setTipo(datos.getTipo());
        return servicioRepository.save(servicio);
    }

    public void delete(Long id) {
        if (!servicioRepository.existsById(id)) {
            throw new RuntimeException("Servicio no encontrado con id: " + id);
        }
        servicioRepository.deleteById(id);
    }
}
