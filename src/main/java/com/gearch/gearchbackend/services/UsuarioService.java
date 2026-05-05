package com.gearch.gearchbackend.services;

import com.gearch.gearchbackend.models.Taller;
import com.gearch.gearchbackend.models.Usuario;
import com.gearch.gearchbackend.enums.RolUsuario;
import com.gearch.gearchbackend.repositories.TallerRepository;
import com.gearch.gearchbackend.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TallerRepository tallerRepository;

    public List<Usuario> findAll() {
        return usuarioRepository.findAll();
    }

    public Usuario findById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        return usuarioRepository.getReferenceById(id);
    }

    public Usuario registrarCliente(Usuario usuario) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        usuario.setRol(RolUsuario.CLIENTE);
        usuario.setTallerAdministrado(null);
        return usuarioRepository.save(usuario);
    }

    public Usuario registrarAdminTaller(Usuario usuario, Taller taller) {
        if (usuarioRepository.existsByEmail(usuario.getEmail())) {
            throw new RuntimeException("Ya existe un usuario con el email: " + usuario.getEmail());
        }
        Taller tallerGuardado = tallerRepository.save(taller);//devuelve taller con id ya asignada
        usuario.setRol(RolUsuario.ADMIN_TALLER);
        usuario.setTallerAdministrado(tallerGuardado);//le asignamos el taller que ya tiene la id
        return usuarioRepository.save(usuario);
    }

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

    public void delete(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado con id: " + id);
        }
        usuarioRepository.deleteById(id);
    }

    public Usuario login(String email, String password) {
        //aqui se verifica la contraseña, repository devuelve obj java del sql y luego el controller los convierte en json
        Usuario usuario = usuarioRepository.findByEmail(email);
        if (usuario == null) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }
        if (!usuario.getPassword().equals(password)) {
            throw new RuntimeException("Email o contraseña incorrectos");
        }
        return usuario;
    }
}
