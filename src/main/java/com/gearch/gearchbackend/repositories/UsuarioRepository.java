package com.gearch.gearchbackend.repositories;


import com.gearch.gearchbackend.entities.Usuario;
import com.gearch.gearchbackend.enums.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);

    // Buscar todos los usuarios de un rol concreto
    List<Usuario> findByRol(RolUsuario rol);

    // Buscar el admin vinculado a un taller concreto
    Optional<Usuario> findByTallerAdministradoId(Long tallerId);
}
