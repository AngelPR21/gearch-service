package com.gearch.gearchbackend.repositories;

import com.gearch.gearchbackend.models.Usuario;
import com.gearch.gearchbackend.enums.RolUsuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByEmail(String email);
    boolean existsByEmail(String email);
    List<Usuario> findByRol(RolUsuario rol);
    Usuario findByTallerAdministradoId(Long tallerId);
}
