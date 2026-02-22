package com.antigiro.antigiro.services;

import com.antigiro.antigiro.models.PeriodoRecoleccion;
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
    public PeriodoRecoleccion obtenerOCrearPeriodoActivoSinUsuario() {
        return repository.findFirstByEstadoOrderByFechaIngresoDesc("ACTIVO")
                .orElseGet(this::crearNuevoPeriodoSinUsuario);
    }

    @Transactional
    public PeriodoRecoleccion crearNuevoPeriodoSinUsuario() {
        PeriodoRecoleccion periodo = new PeriodoRecoleccion();
        periodo.setFechaIngreso(LocalDateTime.now());
        periodo.setFechaFin(LocalDate.now().plusMonths(6));
        periodo.setEstado("ACTIVO");
        periodo.setPesoTotal(BigDecimal.ZERO); 
        return repository.save(periodo);
    }

    @Transactional 
    public void actualizarPesoTotal(Long periodoId, BigDecimal pesoTotal) {  
        PeriodoRecoleccion periodo = repository.findById(periodoId) 
        .orElseThrow(() -> new RuntimeException("Periodo no encontrado")); 
        periodo.setPesoTotal(pesoTotal); 
        repository.save(periodo); 
    }

    @Transactional
    public PeriodoRecoleccion cerrarPeriodoSinUsuario(Long id) {
        PeriodoRecoleccion periodo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Período no encontrado"));
        
        periodo.setEstado("CERRADO");
        periodo.setFechaCierre(LocalDateTime.now());
        
        return repository.save(periodo);
    }
}