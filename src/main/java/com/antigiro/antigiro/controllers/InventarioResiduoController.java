package com.antigiro.antigiro.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antigiro.antigiro.models.InventarioResiduo;
import com.antigiro.antigiro.models.PeriodoRecoleccion;
import com.antigiro.antigiro.models.User;
import com.antigiro.antigiro.services.CustomUserDetailsService;
import com.antigiro.antigiro.services.InventarioResiduoService;
import com.antigiro.antigiro.services.NotificacionService;
import com.antigiro.antigiro.services.PeriodoRecoleccionService;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class InventarioResiduoController {

    @Autowired
    private InventarioResiduoService service;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;
    @Autowired
    private PeriodoRecoleccionService periodoService;
    @Autowired
    private NotificacionService notificacionService;

    @GetMapping("/periodo/{periodoId}")
    public List<InventarioResiduo> listarPorPeriodo(@PathVariable Long periodoId) {
        return service.listarPorPeriodo(periodoId);
    }

    @PostMapping("/agregar")
    public ResponseEntity<Map<String, Object>> agregar(
        @RequestBody Map<String, Object> payload,
        Authentication authentication) {
        User User = obtenerUserAutenticado(authentication);
        Long residuoId = Long.valueOf(payload.get("residuoId").toString());
        Integer cantidad = Integer.valueOf(payload.get("cantidad").toString());
        
        InventarioResiduo inventario = service.agregarOActualizar(residuoId, cantidad, User);
        PeriodoRecoleccion periodo = periodoService.obtenerActivo();
        NotificacionService.EmailResultado emailResultado = notificacionService.verificarLlenado(periodo);
        return ResponseEntity.ok(Map.of(
            "inventario", inventario,
            "emailResultado", emailResultado
        ));
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
    
    private User obtenerUserAutenticado(Authentication authentication) {
        String email = authentication.getName();
        return userDetailsService.loadUserByEmail(email);
    }
}