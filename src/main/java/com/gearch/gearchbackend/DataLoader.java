package com.gearch.gearchbackend;

import com.gearch.gearchbackend.entities.*;
import com.gearch.gearchbackend.enums.DiaSemana;
import com.gearch.gearchbackend.enums.EstadoCita;
import com.gearch.gearchbackend.enums.RolUsuario;
import com.gearch.gearchbackend.enums.TipoServicio;
import com.gearch.gearchbackend.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Inserta datos de ejemplo al arrancar la aplicación.
 * Solo se ejecuta si la base de datos está vacía.
 */
@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UsuarioRepository              usuarioRepository;
    private final TallerRepository               tallerRepository;
    private final VehiculoRepository             vehiculoRepository;
    private final ServicioRepository             servicioRepository;
    private final CitaRepository                 citaRepository;
    private final ResenaRepository               resenaRepository;
    private final DisponibilidadTallerRepository disponibilidadRepository;

    @Override
    public void run(String... args) {

        if (usuarioRepository.count() > 0) return;

        // ── Talleres ──────────────────────────────────────────────
        Taller t1 = tallerRepository.save(Taller.builder()
                .nombre("AutoTop Valencia")
                .direccion("Calle Motor 12, Valencia")
                .telefono("963112233")
                .descripcion("Taller de confianza en el centro de Valencia")
                .latitud(39.4699).longitud(-0.3763)
                .build());

        Taller t2 = tallerRepository.save(Taller.builder()
                .nombre("MecánicaExpress")
                .direccion("Avenida del Cid 45, Valencia")
                .telefono("961223344")
                .descripcion("Reparaciones rápidas y económicas")
                .latitud(39.4750).longitud(-0.3800)
                .build());

        // ── Usuarios CLIENTE ──────────────────────────────────────
        Usuario u1 = usuarioRepository.save(Usuario.builder()
                .nombre("Carlos").apellidos("Martínez López")
                .email("carlos@email.com").password("1234")
                .telefono("612345678").rol(RolUsuario.CLIENTE).build());

        Usuario u2 = usuarioRepository.save(Usuario.builder()
                .nombre("Ana").apellidos("García Ruiz")
                .email("ana@email.com").password("1234")
                .telefono("698765432").rol(RolUsuario.CLIENTE).build());

        // ── Usuarios ADMIN_TALLER ─────────────────────────────────
        usuarioRepository.save(Usuario.builder()
                .nombre("Pedro").apellidos("Sánchez")
                .email("admin.autotop@email.com").password("admin1234")
                .telefono("963112233").rol(RolUsuario.ADMIN_TALLER)
                .tallerAdministrado(t1).build());

        usuarioRepository.save(Usuario.builder()
                .nombre("Laura").apellidos("Gómez")
                .email("admin.mecanica@email.com").password("admin1234")
                .telefono("961223344").rol(RolUsuario.ADMIN_TALLER)
                .tallerAdministrado(t2).build());

        // ── Horario semanal — AutoTop (Lun-Vie 08:30-18:00, Sáb 09:00-13:00)
        DiaSemana[] diasLaborables = {
            DiaSemana.LUNES, DiaSemana.MARTES, DiaSemana.MIERCOLES,
            DiaSemana.JUEVES, DiaSemana.VIERNES
        };
        for (DiaSemana dia : diasLaborables) {
            disponibilidadRepository.save(DisponibilidadTaller.builder()
                    .diaSemana(dia)
                    .horaInicio(LocalTime.of(8, 30))
                    .horaFin(LocalTime.of(18, 0))
                    .intervaloMinutos(30)
                    .taller(t1).build());
        }
        disponibilidadRepository.save(DisponibilidadTaller.builder()
                .diaSemana(DiaSemana.SABADO)
                .horaInicio(LocalTime.of(9, 0))
                .horaFin(LocalTime.of(13, 0))
                .intervaloMinutos(30)
                .taller(t1).build());

        // ── Horario semanal — MecánicaExpress (Lun-Sáb 09:00-19:00)
        DiaSemana[] diasT2 = {
            DiaSemana.LUNES, DiaSemana.MARTES, DiaSemana.MIERCOLES,
            DiaSemana.JUEVES, DiaSemana.VIERNES, DiaSemana.SABADO
        };
        for (DiaSemana dia : diasT2) {
            disponibilidadRepository.save(DisponibilidadTaller.builder()
                    .diaSemana(dia)
                    .horaInicio(LocalTime.of(9, 0))
                    .horaFin(LocalTime.of(19, 0))
                    .intervaloMinutos(30)
                    .taller(t2).build());
        }

        // ── Servicios ─────────────────────────────────────────────
        Servicio s1 = servicioRepository.save(Servicio.builder()
                .nombre("Cambio de aceite y filtros")
                .descripcion("Aceite sintético 5W40 + filtro aceite + filtro aire")
                .precio(59.99).duracionMinutos(30)
                .tipo(TipoServicio.CAMBIO_ACEITE).taller(t1).build());

        Servicio s2 = servicioRepository.save(Servicio.builder()
                .nombre("Revisión general 20 puntos")
                .descripcion("Revisión completa del vehículo")
                .precio(89.99).duracionMinutos(60)
                .tipo(TipoServicio.REVISION_GENERAL).taller(t1).build());

        Servicio s3 = servicioRepository.save(Servicio.builder()
                .nombre("Sustitución pastillas de freno")
                .descripcion("Pastillas delanteras o traseras")
                .precio(120.00).duracionMinutos(45)
                .tipo(TipoServicio.FRENOS).taller(t1).build());

        Servicio s4 = servicioRepository.save(Servicio.builder()
                .nombre("Diagnóstico electrónico")
                .descripcion("Lectura de errores con herramienta OBD2")
                .precio(35.00).duracionMinutos(20)
                .tipo(TipoServicio.DIAGNOSTICO).taller(t2).build());

        Servicio s5 = servicioRepository.save(Servicio.builder()
                .nombre("Cambio de neumáticos (4 ruedas)")
                .descripcion("Montaje, equilibrado y válvulas incluidos")
                .precio(160.00).duracionMinutos(60)
                .tipo(TipoServicio.NEUMATICOS).taller(t2).build());

        // ── Vehículos ─────────────────────────────────────────────
        Vehiculo v1 = vehiculoRepository.save(Vehiculo.builder()
                .marca("Seat").modelo("Ibiza").matricula("1234ABC")
                .anio(2018).color("Rojo").combustible("GASOLINA")
                .usuario(u1).build());

        Vehiculo v2 = vehiculoRepository.save(Vehiculo.builder()
                .marca("Toyota").modelo("Corolla").matricula("5678DEF")
                .anio(2021).color("Blanco").combustible("HIBRIDO")
                .usuario(u2).build());

        // ── Citas ─────────────────────────────────────────────────
        citaRepository.save(Cita.builder()
                .fechaHora(LocalDateTime.of(2025, 6, 10, 10, 0))
                .estado(EstadoCita.CONFIRMADA)
                .notas("Traer el coche con el depósito lleno")
                .usuario(u1).taller(t1).servicio(s1).vehiculo(v1).build());

        citaRepository.save(Cita.builder()
                .fechaHora(LocalDateTime.of(2025, 6, 12, 12, 0))
                .estado(EstadoCita.PENDIENTE)
                .usuario(u2).taller(t2).servicio(s4).vehiculo(v2).build());

        // ── Reseñas ───────────────────────────────────────────────
        resenaRepository.save(Resena.builder()
                .comentario("Muy buen trato y trabajo rápido. Totalmente recomendable.")
                .puntuacion(5).fecha(LocalDateTime.now())
                .usuario(u1).taller(t1).build());

        resenaRepository.save(Resena.builder()
                .comentario("El diagnóstico fue correcto pero tardaron un poco más de lo previsto.")
                .puntuacion(4).fecha(LocalDateTime.now())
                .usuario(u2).taller(t2).build());

        System.out.println("✅ Datos de ejemplo cargados correctamente.");
    }
}
