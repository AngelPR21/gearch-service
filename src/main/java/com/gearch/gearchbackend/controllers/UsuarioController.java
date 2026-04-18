package com.gearch.gearchbackend.controllers;

import com.gearch.gearchbackend.models.Taller;
import com.gearch.gearchbackend.models.Usuario;
import com.gearch.gearchbackend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    // GET /api/usuarios
    @GetMapping
    public ResponseEntity<List<Usuario>> getAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }

    // GET /api/usuarios/{id}
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(usuarioService.findById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/usuarios/registro/cliente
    @PostMapping("/registro/cliente")
    public ResponseEntity<?> registroCliente(@RequestBody Usuario usuario) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.registrarCliente(usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // POST /api/usuarios/registro/taller
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
    // Devuelve el usuario con su rol y, si es admin, el id del taller que gestiona
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        try {
            Usuario u = usuarioService.login(
                    credenciales.get("email"),
                    credenciales.get("password")
            );
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
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(usuarioService.update(id, usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

    record RegistroTallerRequest(Usuario usuario, Taller taller) {}
}
