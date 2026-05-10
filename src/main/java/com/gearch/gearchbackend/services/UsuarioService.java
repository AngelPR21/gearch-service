package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.Taller;
import com.gearch.gearchbackend.models.Usuario;
import com.gearch.gearchbackend.enums.RolUsuario;
import com.gearch.gearchbackend.repositories.TallerRepository;
import com.gearch.gearchbackend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TallerRepository tallerRepository;

    // Busca un usuario por id, lanza excepcion si no existe
    public Usuario findById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        return usuarioRepository.getReferenceById(id);
    }

    // Registra un nuevo cliente validando que el email no este en uso
    public Usuario registrarCliente(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        usuario.setRol(RolUsuario.CLIENTE);
        return usuarioRepository.save(usuario);
    }

    // Registra un nuevo admin de taller
    // Primero guarda el taller para obtener su id, luego lo asigna al usuario
    public Usuario registrarAdminTaller(Usuario usuario, Taller taller) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        Taller tallerGuardado = tallerRepository.save(taller);
        usuario.setRol(RolUsuario.ADMIN_TALLER);
        usuario.setTallerAdministrado(tallerGuardado);
        return usuarioRepository.save(usuario);
    }

    // Actualiza los datos del perfil de un usuario
    public Usuario update(Long id, Usuario datos) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        Usuario usuario = usuarioRepository.getReferenceById(id);
        usuario.setNombre(datos.getNombre());
        usuario.setApellidos(datos.getApellidos());
        usuario.setTelefono(datos.getTelefono());
        return usuarioRepository.save(usuario);
    }

    // Elimina un usuario y todos sus datos relacionados
    // Si es admin de taller tambien se borra el taller y todo lo que contiene (cascada)
    public void delete(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + id));
        if (usuario.getTallerAdministrado() != null) {
            tallerRepository.deleteById(usuario.getTallerAdministrado().getId());
        }
        usuarioRepository.deleteById(id);
    }

    // Verifica las credenciales del usuario para el login
    public Usuario login(String email, String password) {
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Email o contrasena incorrectos");
        }
        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Email o contrasena incorrectos");
        }
        return usuario;
    }
}
