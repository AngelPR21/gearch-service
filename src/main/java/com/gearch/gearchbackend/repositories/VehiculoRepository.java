package com.gearch.gearchbackend.repositories;

import com.gearch.gearchbackend.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {
    List<Vehiculo> findByUsuarioId(Long usuarioId);
    boolean existsByMatricula(String matricula);
}
