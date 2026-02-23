package com.antigiro.antigiro.controllers;

import com.antigiro.antigiro.models.PeriodoRecoleccion;
import com.antigiro.antigiro.models.User;
import com.antigiro.antigiro.services.CustomUserDetailsService;
import com.antigiro.antigiro.services.PeriodoRecoleccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periodos")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PeriodoRecoleccionController {

    @Autowired
    private PeriodoRecoleccionService service;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @GetMapping
    public List<PeriodoRecoleccion> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/activo")
    public ResponseEntity<PeriodoRecoleccion> obtenerActivo(Authentication authentication) {
        User User = obtenerUserAutenticado(authentication);
        PeriodoRecoleccion periodo = service.obtenerOCrearPeriodoActivo(User);
        return ResponseEntity.ok(periodo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodoRecoleccion> obtenerPorId(@PathVariable Long id) {
        PeriodoRecoleccion periodo = service.obtenerPorId(id);
        return periodo != null ? ResponseEntity.ok(periodo) : ResponseEntity.notFound().build();
    }

    @PostMapping("/cerrar/{id}")
    public ResponseEntity<PeriodoRecoleccion> cerrarPeriodo(
            @PathVariable Long id,
            Authentication authentication) {
        User User = obtenerUserAutenticado(authentication);
        PeriodoRecoleccion periodo = service.cerrarPeriodo(id, User);
        return ResponseEntity.ok(periodo);
    }
    
    private User obtenerUserAutenticado(Authentication authentication) {
        String email = authentication.getName();
        return userDetailsService.loadUserByEmail(email);
    }
}