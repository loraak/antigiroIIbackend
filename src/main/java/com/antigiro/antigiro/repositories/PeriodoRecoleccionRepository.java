package com.antigiro.antigiro.repositories;

import com.antigiro.antigiro.models.PeriodoRecoleccion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface PeriodoRecoleccionRepository extends JpaRepository<PeriodoRecoleccion, Long> {
    Optional<PeriodoRecoleccion> findFirstByEstadoOrderByFechaIngresoDesc(String estado);
}