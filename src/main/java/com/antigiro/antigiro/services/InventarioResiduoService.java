package com.antigiro.antigiro.services;

import com.antigiro.antigiro.models.InventarioResiduo;
import com.antigiro.antigiro.models.PeriodoRecoleccion;
import com.antigiro.antigiro.models.Residuo;
import com.antigiro.antigiro.models.User; 
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
    public InventarioResiduo agregarOActualizar(Long residuoId, Integer cantidad, User usuario) {
        // Obtener o crear período activo
        PeriodoRecoleccion periodoActivo = periodoService.obtenerOCrearPeriodoActivo(usuario);
        
        // Buscar si ya existe este residuo en el inventario del período actual
        InventarioResiduo inventario = repository
                .findByPeriodoIdAndResiduoId(periodoActivo.getId(), residuoId)
                .orElse(null);
        
        Residuo residuo = residuoRepository.findById(residuoId)
                .orElseThrow(() -> new RuntimeException("Residuo no encontrado"));
        
        if (inventario == null) {
            // Crear nuevo registro
            inventario = new InventarioResiduo();
            inventario.setPeriodo(periodoActivo);
            inventario.setResiduo(residuo);
            inventario.setCantidad(cantidad);
            inventario.setUsuarioRegistro(usuario);
        } else {
            // Actualizar cantidad existente
            inventario.setCantidad(inventario.getCantidad() + cantidad);
        }
        
        return repository.save(inventario);
    }

    @Transactional
    public InventarioResiduo actualizarCantidad(Long id, Integer nuevaCantidad) {
        InventarioResiduo inventario = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Inventario no encontrado"));
        
        inventario.setCantidad(nuevaCantidad);
        return repository.save(inventario);
    }

    @Transactional
    public void eliminar(Long id) {
        repository.deleteById(id);
    }

    public BigDecimal calcularPesoTotalPeriodo(Long periodoId) {
        List<InventarioResiduo> inventarios = repository.findByPeriodoId(periodoId);
        return inventarios.stream()
                .map(InventarioResiduo::getPesoTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}