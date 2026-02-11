package com.antigiro.antigiro.controllers;


import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;
import com.antigiro.antigiro.models.Residuo;
import com.antigiro.antigiro.repositories.ResiduoRepository;


@RestController
@RequestMapping("/api/residuos")
@CrossOrigin(origins = "http://localhost:4200")
public class ResiduoController {

    private final ResiduoRepository repository;

    public ResiduoController(ResiduoRepository repository) {
        this.repository = repository;
    }

    // ========================
    // GET - Listar todos
    // ========================
    @GetMapping
    public List<Residuo> listar() {
        return repository.findAll();
    }

    // ========================
    // GET - Buscar por ID
    // ========================
    @GetMapping("/{id}")
    public Residuo obtener(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Residuo no encontrado"));
    }

    // ========================
    // POST - Crear
    // ========================
    @PostMapping
    public Residuo crear(@RequestBody Residuo residuo) {

        // Si Angular no envía fecha, la asignamos automáticamente
        if (residuo.getFecha() == null) {
            residuo.setFecha(LocalDate.now());
        }

        return repository.save(residuo);
    }

    // ========================
    // PUT - Actualizar
    // ========================
    @PutMapping("/{id}")
    public Residuo actualizar(@PathVariable Long id, @RequestBody Residuo residuo) {

        Residuo existente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Residuo no encontrado"));

        existente.setNombre(residuo.getNombre());
        existente.setPesoUnitario(residuo.getPesoUnitario());
        existente.setCantidad(residuo.getCantidad());
        existente.setFecha(residuo.getFecha());

        return repository.save(existente);
    }

    // ========================
    // DELETE - Eliminar
    // ========================
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
