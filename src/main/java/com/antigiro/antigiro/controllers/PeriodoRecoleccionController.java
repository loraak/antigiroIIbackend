package com.antigiro.antigiro.controllers;

import com.antigiro.antigiro.models.PeriodoRecoleccion;
import com.antigiro.antigiro.services.PeriodoRecoleccionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/periodos")
@CrossOrigin(origins = "*")
public class PeriodoRecoleccionController {

    @Autowired
    private PeriodoRecoleccionService service;

    @GetMapping
    public List<PeriodoRecoleccion> listarTodos() {
        return service.listarTodos();
    }

    @GetMapping("/activo")
    public ResponseEntity<PeriodoRecoleccion> obtenerActivo() {
        PeriodoRecoleccion periodo = service.obtenerOCrearPeriodoActivoSinUsuario();
        return ResponseEntity.ok(periodo);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PeriodoRecoleccion> obtenerPorId(@PathVariable Long id) {
        PeriodoRecoleccion periodo = service.obtenerPorId(id);
        return periodo != null ? ResponseEntity.ok(periodo) : ResponseEntity.notFound().build();
    }

    @PostMapping("/cerrar/{id}")
    public ResponseEntity<PeriodoRecoleccion> cerrarPeriodo(@PathVariable Long id) {
        PeriodoRecoleccion periodo = service.cerrarPeriodoSinUsuario(id);
        return ResponseEntity.ok(periodo);
    }
}