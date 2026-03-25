package com.gearch.gearchbackend.controllers;


import com.gearch.gearchbackend.entities.Taller;
import com.gearch.gearchbackend.entities.Usuario;
import com.gearch.gearchbackend.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<Usuario> getById(@PathVariable Long id) {
        return usuarioService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ── REGISTRO CLIENTE ──────────────────────────────────────────────────────
    // POST /api/usuarios/registro/cliente
    // Body: { "nombre":"...", "apellidos":"...", "email":"...", "password":"...", "telefono":"..." }
    @PostMapping("/registro/cliente")
    public ResponseEntity<?> registroCliente(@RequestBody Usuario usuario) {
        try {
            Usuario nuevo = usuarioService.registrarCliente(usuario);
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── REGISTRO ADMIN TALLER ─────────────────────────────────────────────────
    // POST /api/usuarios/registro/taller
    // Body: {
    //   "usuario": { "nombre":"...", "apellidos":"...", "email":"...", "password":"..." },
    //   "taller":  { "nombre":"...", "direccion":"...", "telefono":"...", "latitud":0.0, "longitud":0.0 }
    // }
    @PostMapping("/registro/taller")
    public ResponseEntity<?> registroAdminTaller(@RequestBody RegistroTallerRequest request) {
        try {
            Usuario nuevo = usuarioService.registrarAdminTaller(request.usuario(), request.taller());
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevo);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ── LOGIN (sirve para ambos roles) ────────────────────────────────────────
    // POST /api/usuarios/login
    // Body: { "email": "...", "password": "..." }
    // Respuesta incluye el campo "rol" (CLIENTE o ADMIN_TALLER) para que Android
    // redirija a la pantalla correcta, y "tallerAdministradoId" si es admin.
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> credenciales) {
        String email = credenciales.get("email");
        String password = credenciales.get("password");

        return usuarioService.login(email, password)
                .map(u -> {
                    // Construimos respuesta con datos útiles para la app Android
                    var respuesta = new java.util.LinkedHashMap<String, Object>();
                    respuesta.put("id", u.getId());
                    respuesta.put("nombre", u.getNombre());
                    respuesta.put("apellidos", u.getApellidos());
                    respuesta.put("email", u.getEmail());
                    respuesta.put("telefono", u.getTelefono());
                    respuesta.put("rol", u.getRol());
                    // Si es admin, devolvemos también el id de su taller
                    if (u.getTallerAdministrado() != null) {
                        respuesta.put("tallerAdministradoId", u.getTallerAdministrado().getId());
                    }
                    return ResponseEntity.ok((Object) respuesta);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of("error", "Email o contraseña incorrectos")));
    }

    // PUT /api/usuarios/{id}
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Usuario usuario) {
        try {
            return ResponseEntity.ok(usuarioService.update(id, usuario));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE /api/usuarios/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            usuarioService.delete(id);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado correctamente"));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Record auxiliar para el body del registro de taller
    record RegistroTallerRequest(Usuario usuario, Taller taller) {}
}

