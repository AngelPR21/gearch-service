package com.gearch.gearchbackend.repositories;

import com.gearch.gearchbackend.models.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {
    List<Resena> findByTallerId(Long tallerId);
    List<Resena> findByUsuarioId(Long usuarioId);
}
