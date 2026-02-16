package com.antigiro.antigiro.controllers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.antigiro.antigiro.models.Residuo;
import com.antigiro.antigiro.repositories.ResiduoRepository;

@RestController
@RequestMapping("/api/residuos")
@CrossOrigin(origins = "http://localhost:4200")
public class ResiduoController {

    @Autowired
    private ResiduoRepository repository; 

    @GetMapping
    public List<Residuo> getAll() { 
        return repository.findAll(); 
    }

    @PostMapping
    public Residuo create(@RequestBody Residuo residuo) { 
        return repository.save(residuo); 
    }

    @GetMapping("/{id}")
    public ResponseEntity<Residuo> getById(@PathVariable Long id) { 
        return repository.findById(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Residuo> update(@PathVariable Long id, @RequestBody Residuo detalles) {
        return repository.findById(id).map(residuo -> {
            residuo.setNombre(detalles.getNombre());
            residuo.setPesoUnitario(detalles.getPesoUnitario());
            residuo.setDescripcion(detalles.getDescripcion());
            residuo.setImagen(detalles.getImagen());
            residuo.setUsuarioActualizacion(detalles.getUsuarioActualizacion());
            
            Residuo actualizado = repository.save(residuo);
            return ResponseEntity.ok(actualizado);
        }).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Residuo> delete(@PathVariable Long id) {
        return repository.findById(id).map(residuo -> {
            Integer estadoActual = residuo.getEstado();
            residuo.setEstado(Integer.valueOf(0).equals(estadoActual) ? 1 : 0);
            Residuo residuoActualizado = repository.save(residuo);
            return ResponseEntity.ok(residuoActualizado);
        }).orElse(ResponseEntity.notFound().build());
    }
}
