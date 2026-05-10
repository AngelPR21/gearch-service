package com.gearch.gearchbackend.repositories;

import com.gearch.gearchbackend.models.Taller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TallerRepository extends JpaRepository<Taller, Long> {

    // Busca talleres cuyo nombre contenga el texto indicado, sin distinguir mayusculas
    List<Taller> findByNombreContainingIgnoreCase(String nombre);

    // Calcula la distancia entre las coordenadas del usuario y cada taller usando la formula de Haversine
    // Solo devuelve talleres dentro del radio indicado en kilometros, ordenados de mas cercano a mas lejano
    @Query(value = """
        SELECT * FROM talleres t
        WHERE t.latitud IS NOT NULL AND t.longitud IS NOT NULL
        AND (
            6371 * ACOS(
                COS(RADIANS(:lat)) * COS(RADIANS(t.latitud)) *
                COS(RADIANS(t.longitud) - RADIANS(:lng)) +
                SIN(RADIANS(:lat)) * SIN(RADIANS(t.latitud))
            )
        ) <= :radioKm
        ORDER BY (
            6371 * ACOS(
                COS(RADIANS(:lat)) * COS(RADIANS(t.latitud)) *
                COS(RADIANS(t.longitud) - RADIANS(:lng)) +
                SIN(RADIANS(:lat)) * SIN(RADIANS(t.latitud))
            )
        ) ASC
        """, nativeQuery = true)
    List<Taller> findTalleresCercanos(
            @Param("lat") double latitud,
            @Param("lng") double longitud,
            @Param("radioKm") double radioKm
    );
}
