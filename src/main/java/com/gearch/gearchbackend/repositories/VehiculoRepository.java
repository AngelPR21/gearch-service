package com.gearch.gearchbackend.repositories;


import com.gearch.gearchbackend.entities.Vehiculo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VehiculoRepository extends JpaRepository<Vehiculo, Long> {

    List<Vehiculo> findByUsuarioId(Long usuarioId);

    Optional<Vehiculo> findByMatricula(String matricula);

    boolean existsByMatricula(String matricula);
}
