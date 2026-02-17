package com.antigiro.antigiro.repositories;

import com.antigiro.antigiro.models.InventarioResiduo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface InventarioResiduoRepository extends JpaRepository<InventarioResiduo, Long> {
    List<InventarioResiduo> findByPeriodoId(Long periodoId);
    Optional<InventarioResiduo> findByPeriodoIdAndResiduoId(Long periodoId, Long residuoId);
}