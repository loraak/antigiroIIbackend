package com.antigiro.antigiro.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antigiro.antigiro.models.PeriodoRecoleccion;
import com.antigiro.antigiro.models.User;
import com.antigiro.antigiro.services.CustomUserDetailsService;
import com.antigiro.antigiro.services.NotificacionService;
import com.antigiro.antigiro.services.PeriodoRecoleccionService;

@RestController
@RequestMapping("/api/periodos")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class PeriodoRecoleccionController {

    @Autowired
    private PeriodoRecoleccionService service;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private NotificacionService notiService;

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
    public ResponseEntity<?> cerrarPeriodo(
            @PathVariable Long id,
            Authentication authentication) {
        User User = obtenerUserAutenticado(authentication);
        PeriodoRecoleccion periodoId = service.obtenerPorId(id);
        NotificacionService.EmailResultado emailResultado = notiService.notificarCierre(periodoId);
        PeriodoRecoleccion periodo = service.cerrarPeriodo(id, User);
        return ResponseEntity.ok(Map.of(
        "periodo", periodo,
        "emailResultado", emailResultado
    ));
    }
    
    private User obtenerUserAutenticado(Authentication authentication) {
        String email = authentication.getName();
        return userDetailsService.loadUserByEmail(email);
    }
}