package com.antigiro.antigiro.models;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "residuos")
public class Residuo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(name = "peso_unitario", nullable = false)
    private Double pesoUnitario;

    private String descripcion;

    private String imagen; 

    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @Column(name = "usuario_ingreso")
    private Integer usuarioIngreso;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @Column(name = "usuario_actualizacion")
    private Integer usuarioActualizacion;

    @Column(columnDefinition = "int default 1")
    private Integer estado;

    @PrePersist
    protected void onCreate() {
        this.fechaIngreso = LocalDateTime.now();
        if (this.estado == null) {
            this.estado = 1;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
    }
}