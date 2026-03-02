package com.antigiro.antigiro.controllers;

import com.antigiro.antigiro.models.User;
import com.antigiro.antigiro.services.CustomUserDetailsService;
import com.antigiro.antigiro.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
public class UserController {

    @Autowired
    private UserService servicio;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private CustomUserDetailsService userDetailsService;

    @PostMapping("/registro")
    public ResponseEntity<?> registrar(@RequestBody User User) {
        try {
            User nuevoUser = servicio.registrar(User);
            return ResponseEntity.ok(nuevoUser);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al registrar User");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginRequest.getEmail(), 
                    loginRequest.getContrasena()
                )
            );
            
            SecurityContextHolder.getContext().setAuthentication(authentication);
            
            HttpSession session = request.getSession(true);
            session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                SecurityContextHolder.getContext()
            );
            
            User User = userDetailsService.loadUserByEmail(loginRequest.getEmail());
            User.setContrasena(null);
            
            return ResponseEntity.ok(User);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas: " + e.getMessage());
        }
    }
    
    @GetMapping("/current")
    public ResponseEntity<User> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal())) {
            String email = authentication.getName();
            User User = userDetailsService.loadUserByEmail(email);
            User.setContrasena(null);
            return ResponseEntity.ok(User);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
    
    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok("Logout exitoso");
    }

    //Administrativo. 
    @GetMapping("/administrativo")
    public List<User> listarUsuarios() {
        return servicio.listarUsuarios();
    }

    
}