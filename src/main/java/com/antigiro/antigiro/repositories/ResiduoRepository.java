package com.antigiro.antigiro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.antigiro.antigiro.models.Residuo;

@Repository
public interface ResiduoRepository extends JpaRepository<Residuo, Long> {
}