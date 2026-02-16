package com.antigiro.antigiro.models;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
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

    @Column(name = "peso_unitario", nullable = false, precision = 10, scale = 2)
    private BigDecimal pesoUnitario;

    private String descripcion;

    @Column(columnDefinition = "LONGTEXT")
    private String imagen; 

    @Column(name = "fecha_ingreso")
    private LocalDateTime fechaIngreso;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_ingreso", referencedColumnName = "id")
    private User usuarioIngreso;

    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_actualizacion", referencedColumnName = "id")
    private User usuarioActualizacion;

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