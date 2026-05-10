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

    // Todos los talleres
    public List<Taller> findAll() {
        return tallerRepository.findAll();
    }

    // Busca un taller por id, lanza excepcion si no existe
    public Taller findById(Long id) {
        if (!tallerRepository.existsById(id)) {
            throw new RuntimeException("Taller no encontrado con id: " + id);
        }
        return tallerRepository.getReferenceById(id);
    }

    // Busca talleres por nombre ignorando mayusculas
    public List<Taller> buscarPorNombre(String nombre) {
        return tallerRepository.findByNombreContainingIgnoreCase(nombre);
    }

    // Busca talleres dentro de un radio en kilometros usando la formula de Haversine
    public List<Taller> buscarCercanos(double latitud, double longitud, double radioKm) {
        return tallerRepository.findTalleresCercanos(latitud, longitud, radioKm);
    }

    // Actualiza la foto de perfil del taller
    // Si foto es null se borra la foto y el frontend mostrara la imagen por defecto
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
