package com.antigiro.antigiro.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.antigiro.antigiro.models.User;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import com.antigiro.antigiro.services.UserService;


@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UserController {
    
    @Autowired 
    private UserService servicio; 

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody User user) { 
        try { 
            User usuarioRegistrado = servicio.registrar(user);
            return ResponseEntity.ok(usuarioRegistrado);
        } catch (Exception e) { 
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) { 
        User user = servicio.login(loginRequest.getEmail(), loginRequest.getContrasena()); 

        if (user != null) { 
            return ResponseEntity.ok(user); 
        } else { 
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }
    
}
