package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.Taller;
import com.gearch.gearchbackend.repositories.TallerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TallerService {

    private final TallerRepository tallerRepository;

    public List<Taller> findAll() {
        return tallerRepository.findAll();
    }

    public Taller findById(Long id) {
        if (!tallerRepository.existsById(id)) {
            throw new RuntimeException("Taller no encontrado con id: " + id);
        }
        return tallerRepository.getReferenceById(id);
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
        if (!tallerRepository.existsById(id)) {
            throw new RuntimeException("Taller no encontrado con id: " + id);
        }
        Taller taller = tallerRepository.getReferenceById(id);
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

    public Taller actualizarFotoPerfil(Long id, MultipartFile foto) throws IOException {
        Taller taller = tallerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Taller no encontrado con id: " + id));

        if (foto != null) {
            if (foto.getSize() > 5 * 1024 * 1024)
                throw new IllegalArgumentException("La imagen no puede superar 5 MB");
            taller.setFotoPerfil(foto.getBytes());
        } else {
            taller.setFotoPerfil(null);
        }

        return tallerRepository.save(taller);
    }
}
