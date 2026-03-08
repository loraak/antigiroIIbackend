package com.antigiro.antigiro.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "usuarios")
public class UserCrud {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    private String nombre; 
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String email; 
    private int rol;
    private int estatus; 
}
