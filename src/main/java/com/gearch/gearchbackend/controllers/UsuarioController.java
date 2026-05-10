package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.models.Taller;
import com.gearch.gearchbackend.models.Usuario;
import com.gearch.gearchbackend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

// Controlador para las operaciones de usuarios: registro, login, perfil y eliminacion
@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // GET /api/usuarios/{id}
    // Devuelve los datos de un usuario por id (usado en PerfilActivity)
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(usuarioService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/usuarios/registro/cliente
    // Registra un nuevo cliente
    @PostMapping("/registro/cliente")
    public ResponseEntity<?> registroCliente(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarCliente(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/usuarios/registro/taller
    // Registra un nuevo admin de taller junto con los datos de su taller
    // El body contiene un objeto con "usuario" y "taller" anidados
    @PostMapping("/registro/taller")
    public ResponseEntity<?> registroAdminTaller(@RequestBody RegistroTallerRequest request) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(usuarioService.registrarAdminTaller(request.usuario(), request.taller()));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/usuarios/login
    // Verifica las credenciales y devuelve los datos del usuario con su rol
    // Si es admin tambien devuelve el id del taller que administra
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        try {
            Usuario u = usuarioService.login(
                    credenciales.get("email"),
                    credenciales.get("password")
            );
            // LinkedHashMap mantiene el orden de insercion de los campos en el JSON
            Map<String, Object> respuesta = new LinkedHashMap<>();
            respuesta.put("id", u.getId());
            respuesta.put("nombre", u.getNombre());
            respuesta.put("apellidos", u.getApellidos());
            respuesta.put("email", u.getEmail());
            respuesta.put("telefono", u.getTelefono());
            respuesta.put("rol", u.getRol());
            if (u.getTallerAdministrado() != null) {
                respuesta.put("tallerAdministradoId", u.getTallerAdministrado().getId());
            }
            return ResponseEntity.ok(respuesta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", e.getMessage()));
        }
    }

    // PUT /api/usuarios/{id}
    // Actualiza el perfil del usuario (nombre, apellidos, telefono)
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(usuarioService.update(id, usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/usuarios/{id}
    // Elimina la cuenta del usuario y todos sus datos relacionados
    // Si es admin tambien elimina su taller y todo lo que contiene
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // Record que actua como contenedor temporal para el registro de admin taller
    // Agrupa el objeto usuario y el objeto taller en un solo body del request
    record RegistroTallerRequest(Usuario usuario, Taller taller) {}
}
