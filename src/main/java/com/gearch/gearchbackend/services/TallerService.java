package com.gearch.gearchbackend.services;


import com.gearch.gearchbackend.entities.Taller;
import com.gearch.gearchbackend.repositories.TallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TallerService {

    private final TallerRepository tallerRepository;

    public List<Taller> findAll() {
        return tallerRepository.findAll();
    }

    public Optional<Taller> findById(Long id) {
        return tallerRepository.findById(id);
    }

    public List<Taller> buscarPorNombre(String nombre) {
        return tallerRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public List<Taller> buscarCercanos(double latitud, double longitud, double radioKm) {
        return tallerRepository.findTalleresCercanos(latitud, longitud, radioKm);
    }

    public Taller save(Taller taller) {
        return tallerRepository.save(taller);
    }

    public Taller update(Long id, Taller datos) {
        Taller taller = tallerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado con id: " + id));
        taller.setNombre(datos.getNombre());
        taller.setDireccion(datos.getDireccion());
        taller.setTelefono(datos.getTelefono());
        taller.setDescripcion(datos.getDescripcion());
        taller.setLatitud(datos.getLatitud());
        taller.setLongitud(datos.getLongitud());
        return tallerRepository.save(taller);
    }

    public void delete(Long id) {
        if (!tallerRepository.existsById(id)) {
            throw new RuntimeException("Taller no encontrado con id: " + id);
        }
        tallerRepository.deleteById(id);
    }
}
