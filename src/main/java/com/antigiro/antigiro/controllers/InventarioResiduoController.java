package com.antigiro.antigiro.controllers;

import com.antigiro.antigiro.models.InventarioResiduo;
import com.antigiro.antigiro.models.User;
import com.antigiro.antigiro.services.InventarioResiduoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioResiduoController {

    @Autowired
    private InventarioResiduoService service;

    @GetMapping("/periodo/{periodoId}")
    public List<InventarioResiduo> listarPorPeriodo(@PathVariable Long periodoId) {
        return service.listarPorPeriodo(periodoId);
    }

    @PostMapping("/agregar")
    public ResponseEntity<InventarioResiduo> agregar(
            @RequestBody Map<String, Object> payload,
            Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        Long residuoId = Long.valueOf(payload.get("residuoId").toString());
        Integer cantidad = Integer.valueOf(payload.get("cantidad").toString());
        
        InventarioResiduo inventario = service.agregarOActualizar(residuoId, cantidad, user);
        return ResponseEntity.ok(inventario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InventarioResiduo> actualizar(
            @PathVariable Long id,
            @RequestBody Map<String, Integer> payload) {
        Integer nuevaCantidad = payload.get("cantidad");
        InventarioResiduo inventario = service.actualizarCantidad(id, nuevaCantidad);
        return ResponseEntity.ok(inventario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        service.eliminar(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/peso-total/{periodoId}")
    public ResponseEntity<BigDecimal> obtenerPesoTotal(@PathVariable Long periodoId) {
        BigDecimal pesoTotal = service.calcularPesoTotalPeriodo(periodoId);
        return ResponseEntity.ok(pesoTotal);
    }
}