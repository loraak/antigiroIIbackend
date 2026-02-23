package com.antigiro.antigiro.services;

import com.antigiro.antigiro.models.PeriodoRecoleccion;
import com.antigiro.antigiro.models.User;
import com.antigiro.antigiro.repositories.PeriodoRecoleccionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class PeriodoRecoleccionService {

    @Autowired
    private PeriodoRecoleccionRepository repository;

    public List<PeriodoRecoleccion> listarTodos() {
        return repository.findAll();
    }

    public PeriodoRecoleccion obtenerPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    @Transactional
    public PeriodoRecoleccion obtenerOCrearPeriodoActivo(User usuario) {
        return repository.findFirstByEstadoOrderByFechaIngresoDesc("ACTIVO")
                .orElseGet(() -> crearNuevoPeriodo(usuario));
    }

    @Transactional
    public PeriodoRecoleccion crearNuevoPeriodo(User usuario) {
        PeriodoRecoleccion periodo = new PeriodoRecoleccion();
        
        LocalDate hoy = LocalDate.now();
        periodo.setFechaCierre(hoy.plusMonths(6).atStartOfDay());
        periodo.setEstado("ACTIVO");
        periodo.setPesoTotal(BigDecimal.ZERO);
        periodo.setFechaIngreso(LocalDateTime.now());
        periodo.setUsuarioIngreso(usuario);
        
        return repository.save(periodo);
    }

    @Transactional
    public PeriodoRecoleccion cerrarPeriodo(Long id, User usuario) {
        PeriodoRecoleccion periodo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Período no encontrado"));
        
        periodo.setEstado("CERRADO");
        periodo.setFechaCierre(LocalDateTime.now());
        periodo.setUsuarioCierre(usuario);
        
        return repository.save(periodo);
    }
    
    @Transactional
    public void actualizarPesoTotal(Long periodoId, BigDecimal pesoTotal) {
        PeriodoRecoleccion periodo = repository.findById(periodoId)
                .orElseThrow(() -> new RuntimeException("Período no encontrado"));
        periodo.setPesoTotal(pesoTotal);
        repository.save(periodo);
    }
}