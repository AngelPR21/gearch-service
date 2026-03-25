package com.gearch.gearchbackend.services;


import com.gearch.gearchbackend.entities.Usuario;
import com.gearch.gearchbackend.entities.Vehiculo;
import com.gearch.gearchbackend.repositories.UsuarioRepository;
import com.gearch.gearchbackend.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Vehiculo> findAll() {
        return vehiculoRepository.findAll();
    }

    public Optional<Vehiculo> findById(Long id) {
        return vehiculoRepository.findById(id);
    }

    public List<Vehiculo> findByUsuario(Long usuarioId) {
        return vehiculoRepository.findByUsuarioId(usuarioId);
    }

    public Vehiculo save(Long usuarioId, Vehiculo vehiculo) {
        if (vehiculoRepository.existsByMatricula(vehiculo.getMatricula())) {
            throw new RuntimeException("Ya existe un vehículo con matrícula: " + vehiculo.getMatricula());
        }
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + usuarioId));
        vehiculo.setUsuario(usuario);
        return vehiculoRepository.save(vehiculo);
    }

    public Vehiculo update(Long id, Vehiculo datos) {
        Vehiculo vehiculo = vehiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vehículo no encontrado con id: " + id));
        vehiculo.setMarca(datos.getMarca());
        vehiculo.setModelo(datos.getModelo());
        vehiculo.setAnio(datos.getAnio());
        vehiculo.setColor(datos.getColor());
        vehiculo.setCombustible(datos.getCombustible());
        return vehiculoRepository.save(vehiculo);
    }

    public void delete(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RuntimeException("Vehículo no encontrado con id: " + id);
        }
        vehiculoRepository.deleteById(id);
    }
}
