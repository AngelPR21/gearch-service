package com.gearch.gearchbackend.services;


import com.gearch.gearchbackend.entities.Taller;
import com.gearch.gearchbackend.entities.Usuario;
import com.gearch.gearchbackend.enums.RolUsuario;
import com.gearch.gearchbackend.repositories.TallerRepository;
import com.gearch.gearchbackend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TallerRepository tallerRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> findById(Long id) {
        return usuarioRepository.findById(id);
    }

    public Optional<Usuario> findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    // Registro como CLIENTE (sin taller)
    public Usuario registrarCliente(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        usuario.setRol(RolUsuario.CLIENTE);
        usuario.setTallerAdministrado(null);
        return usuarioRepository.save(usuario);
    }

    // Registro como ADMIN_TALLER: se crea el taller y el usuario vinculados en un solo paso
    public Usuario registrarAdminTaller(Usuario usuario, Taller taller) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        // 1. Guardar el taller primero
        Taller tallerGuardado = tallerRepository.save(taller);
        // 2. Vincular usuario con taller y asignar rol
        usuario.setRol(RolUsuario.ADMIN_TALLER);
        usuario.setTallerAdministrado(tallerGuardado);
        return usuarioRepository.save(usuario);
    }

    public Usuario update(Long id, Usuario datos) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        usuario.setNombre(datos.getNombre());
        usuario.setApellidos(datos.getApellidos());
        usuario.setTelefono(datos.getTelefono());
        return usuarioRepository.save(usuario);
    }

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    // Login básico: devuelve el usuario si las credenciales son correctas
    public Optional<Usuario> login(String email, String password) {
        return usuarioRepository.findByEmail(email)
                .filter(u -> u.getPassword().equals(password));
    }
}

