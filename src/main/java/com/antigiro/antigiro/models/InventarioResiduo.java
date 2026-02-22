package com.antigiro.antigiro.models;

import java.math.BigDecimal;
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
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "inventario_residuos")
public class InventarioResiduo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "periodo_id", nullable = false)
    private PeriodoRecoleccion periodo;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "residuo_id", nullable = false)
    private Residuo residuo;
    
    @Column(nullable = false)
    private Integer cantidad;
    
    @Column(name = "peso_total", precision = 10, scale = 2)
    private BigDecimal pesoTotal;
    
    @Column(name = "fecha_registro")
    private LocalDateTime fechaRegistro;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_registro")
    private User usuarioRegistro;
    
    @Column(name = "fecha_actualizacion")
    private LocalDateTime fechaActualizacion;
    
    @PrePersist
    protected void onCreate() {
        this.fechaRegistro = LocalDateTime.now();
        calcularPesoTotal(); 
    }
    
    @PreUpdate
    protected void onUpdate() {
        this.fechaActualizacion = LocalDateTime.now();
        calcularPesoTotal(); 
    }

    private void calcularPesoTotal() { 
        if (this.residuo != null && this.cantidad != null) {
            this.pesoTotal = this.residuo.getPesoUnitario()
                .multiply(new BigDecimal(this.cantidad)); 
        }
    }
}