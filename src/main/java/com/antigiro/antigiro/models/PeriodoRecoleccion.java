package com.antigiro.antigiro.models;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "periodos_recoleccion")
public class PeriodoRecoleccion {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;
    
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;
    
    @Column(name = "peso_total", precision = 10, scale = 2)
    private BigDecimal pesoTotal;
    
    @Column(nullable = false)
    private String estado;
    
    @Column(name = "fecha_cierre")
    private LocalDateTime fechaCierre;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_cierre")
    private User usuarioCierre;
    
    @Column(name = "fecha_ingreso", nullable = false, updatable = false)
    private LocalDateTime fechaIngreso;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_ingreso")
    private User usuarioIngreso;

    //  Para que se añada la fecha de inicio al momento de ingresar un residuo al inventario. 
    @PrePersist 
    protected void onCreate() { 
        this.fechaIngreso = LocalDateTime.now();
        if (this.estado == null) { 
            this.estado = "ACTIVO"; 
        }
        if (this.pesoTotal == null) {
            this.pesoTotal = BigDecimal.ZERO; 
        }
    }
}