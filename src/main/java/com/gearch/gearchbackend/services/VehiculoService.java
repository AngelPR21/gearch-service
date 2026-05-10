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

    // Todos los vehiculos de un usuario
    public List<Vehiculo> findByUsuario(Long usuarioId) {
        return vehiculoRepository.findByUsuarioId(usuarioId);
    }

    // Crea un vehiculo validando que la matricula no este en uso y que el usuario exista
    public Vehiculo save(Long usuarioId, Vehiculo vehiculo) {
        if (vehiculoRepository.existsByMatricula(vehiculo.getMatricula())) {
            throw new RuntimeException("Ya existe un vehiculo con la matricula: " + vehiculo.getMatricula());
        }
        if (!usuarioRepository.existsById(usuarioId)) {
            throw new RuntimeException("Usuario no encontrado con id: " + usuarioId);
        }
        vehiculo.setUsuario(usuarioRepository.getReferenceById(usuarioId));
        return vehiculoRepository.save(vehiculo);
    }

    // Actualiza los datos de un vehiculo existente
    public Vehiculo update(Long id, Vehiculo datos) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RuntimeException("Vehiculo no encontrado con id: " + id);
        }
        Vehiculo vehiculo = vehiculoRepository.getReferenceById(id);
        vehiculo.setMarca(datos.getMarca());
        vehiculo.setModelo(datos.getModelo());
        vehiculo.setAnio(datos.getAnio());
        vehiculo.setColor(datos.getColor());
        vehiculo.setCombustible(datos.getCombustible());
        return vehiculoRepository.save(vehiculo);
    }

    // Elimina un vehiculo por id
    public void delete(Long id) {
        if (!vehiculoRepository.existsById(id)) {
            throw new RuntimeException("Vehiculo no encontrado con id: " + id);
        }
        vehiculoRepository.deleteById(id);
    }
}
