package com.antigiro.antigiro.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.antigiro.antigiro.models.InventarioResiduo;

@Repository
public interface InventarioResiduoRepository extends JpaRepository<InventarioResiduo, Long> {
    List<InventarioResiduo> findByPeriodoId(Long periodoId);
    Optional<InventarioResiduo> findByPeriodoIdAndResiduoId(Long periodoId, Long residuoId);

    @Query("SELECT COUNT(i) FROM InventarioResiduo i where i.periodo.id = :periodoId")
    int countByPeriodoId(@Param("periodoId") Long periodoId);
}