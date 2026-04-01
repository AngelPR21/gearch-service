package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.Usuario;
import com.gearch.gearchbackend.models.Vehiculo;
import com.gearch.gearchbackend.repositories.UsuarioRepository;
import com.gearch.gearchbackend.repositories.VehiculoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehiculoService {

    private final VehiculoRepository vehiculoRepository;
    private final UsuarioRepository usuarioRepository;

    public List<Vehiculo> findAll() {
        return vehiculoRepository.findAll();
    }

    public Vehiculo findById(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RuntimeException("Vehículo no encontrado con id: " + id);
        }
        return vehiculoRepository.getReferenceById(id);
    }

    public List<Vehiculo> findByUsuario(Long usuarioId) {
        return vehiculoRepository.findByUsuarioId(usuarioId);
    }

    public Vehiculo save(Long usuarioId, Vehiculo vehiculo) {
        if (vehiculoRepository.existsByMatricula(vehiculo.getMatricula())) {
            throw new RuntimeException("Ya existe un vehículo con la matrícula: " + vehiculo.getMatricula());
        }
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con id: " + usuarioId);
        }
        Usuario usuario = usuarioRepository.getReferenceById(usuarioId);
        vehiculo.setUsuario(usuario);
        return vehiculoRepository.save(vehiculo);
    }

    public Vehiculo update(Long id, Vehiculo datos) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RuntimeException("Vehículo no encontrado con id: " + id);
        }
        Vehiculo vehiculo = vehiculoRepository.getReferenceById(id);
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
