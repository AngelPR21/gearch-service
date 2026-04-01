package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.Resena;
import com.gearch.gearchbackend.repositories.ResenaRepository;
import com.gearch.gearchbackend.repositories.TallerRepository;
import com.gearch.gearchbackend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ResenaService {

    private final ResenaRepository resenaRepository;
    private final UsuarioRepository usuarioRepository;
    private final TallerRepository tallerRepository;

    public List<Resena> findAll() {
        return resenaRepository.findAll();
    }

    public Resena findById(Long id) {
        if (!resenaRepository.existsById(id)) {
            throw new RuntimeException("Reseña no encontrada con id: " + id);
        }
        return resenaRepository.getReferenceById(id);
    }

    public List<Resena> findByTaller(Long tallerId) {
        return resenaRepository.findByTallerId(tallerId);
    }

    public List<Resena> findByUsuario(Long usuarioId) {
        return resenaRepository.findByUsuarioId(usuarioId);
    }

    public Resena save(Long usuarioId, Long tallerId, Resena resena) {
        if (resena.getPuntuacion() < 1 || resena.getPuntuacion() > 5) {
            throw new RuntimeException("La puntuación debe estar entre 1 y 5.");
        }
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con id: " + usuarioId);
        }
        if (!tallerRepository.existsById(tallerId)) {
            throw new RuntimeException("Taller no encontrado con id: " + tallerId);
        }
        resena.setUsuario(usuarioRepository.getReferenceById(usuarioId));
        resena.setTaller(tallerRepository.getReferenceById(tallerId));
        resena.setFecha(LocalDateTime.now());
        return resenaRepository.save(resena);
    }

    public void delete(Long id) {
        if (!resenaRepository.existsById(id)) {
            throw new RuntimeException("Reseña no encontrada con id: " + id);
        }
        resenaRepository.deleteById(id);
    }
}
