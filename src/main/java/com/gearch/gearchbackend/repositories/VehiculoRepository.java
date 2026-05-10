package com.gearch.gearchbackend.repositories;

import com.gearch.gearchbackend.models.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    // Todos los vehiculos de un usuario
    List<Vehiculo> findByUsuarioId(Long usuarioId);

    // Comprueba si ya existe un vehiculo con esa matricula, usado al crear
    boolean existsByMatricula(String matricula);
}
