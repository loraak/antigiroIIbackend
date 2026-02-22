package com.antigiro.antigiro.services;

import com.antigiro.antigiro.models.InventarioResiduo;
import com.antigiro.antigiro.models.PeriodoRecoleccion;
import com.antigiro.antigiro.models.Residuo;
import com.antigiro.antigiro.repositories.InventarioResiduoRepository;
import com.antigiro.antigiro.repositories.ResiduoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class InventarioResiduoService {

    @Autowired
    private InventarioResiduoRepository repository;

    @Autowired
    private ResiduoRepository residuoRepository;

    @Autowired
    private PeriodoRecoleccionService periodoService;

    public List<InventarioResiduo> listarPorPeriodo(Long periodoId) {
        return repository.findByPeriodoId(periodoId);
    }

    @Transactional
    public InventarioResiduo agregarOActualizarSinUsuario(Long residuoId, Integer cantidad) {
        PeriodoRecoleccion periodoActivo = periodoService.obtenerOCrearPeriodoActivoSinUsuario();
        
        InventarioResiduo inventario = repository
                .findByPeriodoIdAndResiduoId(periodoActivo.getId(), residuoId)
                .orElse(null);
        
        Residuo residuo = residuoRepository.findById(residuoId)
                .orElseThrow(() -> new RuntimeException("Residuo no encontrado"));
        
        if (inventario == null) {
            inventario = new InventarioResiduo();
            inventario.setPeriodo(periodoActivo);
            inventario.setResiduo(residuo);
            inventario.setCantidad(cantidad);
        } else {
            inventario.setCantidad(inventario.getCantidad() + cantidad);
        }
        
        InventarioResiduo resultado = repository.save(inventario);
        
        actualizarPesoTotalPeriodo(periodoActivo.getId());
        
        return resultado;
    }

    @Transactional
    public InventarioResiduo actualizarCantidad(Long id, Integer nuevaCantidad) {
        InventarioResiduo inventario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        
        inventario.setCantidad(nuevaCantidad);
        InventarioResiduo resultado = repository.save(inventario);
        
        actualizarPesoTotalPeriodo(inventario.getPeriodo().getId());
        
        return resultado;
    }

    @Transactional
    public void eliminar(Long id) {
        InventarioResiduo inventario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        
        Long periodoId = inventario.getPeriodo().getId();
        repository.deleteById(id);
        
        actualizarPesoTotalPeriodo(periodoId);
    }

    public BigDecimal calcularPesoTotalPeriodo(Long periodoId) {
        List<InventarioResiduo> inventarios = repository.findByPeriodoId(periodoId);
        return inventarios.stream()
                .map(InventarioResiduo::getPesoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
    
    @Transactional
    private void actualizarPesoTotalPeriodo(Long periodoId) {
        BigDecimal pesoTotal = calcularPesoTotalPeriodo(periodoId);
        periodoService.actualizarPesoTotal(periodoId, pesoTotal);
    }
}