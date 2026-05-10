package com.gearch.gearchbackend.repositories;

import com.gearch.gearchbackend.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Busca un usuario por email, usado en el login
    Usuario findByEmail(String email);

    // Comprueba si ya existe un usuario con ese email, usado al registrar
    boolean existsByEmail(String email);
}
