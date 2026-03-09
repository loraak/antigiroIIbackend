package com.antigiro.antigiro.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.antigiro.antigiro.models.User;
import com.antigiro.antigiro.models.UserCrud;
import com.antigiro.antigiro.repositories.UserCrudRepository;
import com.antigiro.antigiro.repositories.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository repositorio; 

    @Autowired
    private UserCrudRepository userCrudRepositorio; 

    @Autowired 
    private BCryptPasswordEncoder encriptador; 

    public User registrar(User user) throws Exception {
        //  Validaciones para saber si los campos no están vacíos.
        if (user.getNombre().isEmpty()) {
            throw new Exception("El nombre es obligatorio");
        }

        if (user.getEmail().isEmpty()) {
            throw new Exception("El email es obligatorio");
        }

        if (user.getContrasena().isEmpty()) {
            throw new Exception("La contraseña es obligatoria");
        }
        
        //  Validación para saber si no hay un email ya usado. 
        if (repositorio.existsByEmail(user.getEmail())) {
            throw new Exception("El email ya está registrado");
        }

    user.setContrasena(encriptador.encode(user.getContrasena()));

    return repositorio.save(user);
}


    public User login(String email, String contrasena) { 
        Optional<User> userOptional = repositorio.findByEmail(email); 

        if (userOptional.isPresent()) { 
            User user = userOptional.get(); 
            if (encriptador.matches(contrasena, user.getContrasena())) {
                user.setContrasena(null);
                return user;
            }
        }
        return null; 
    }

    public List<UserCrud> listarUsuarios() {
        return userCrudRepositorio.findAll();
    }

    public void eliminarUsuario(Long id) { 
        User user = repositorio.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); 
        user.setEstatus(user.getEstatus() == 1 ? 0 : 1); 
        repositorio.save(user); 
    }

    public User editarUsuario(Long id, User datosNuevos) { 
        User user = repositorio.findById(id) 
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado")); 
        user.setNombre(datosNuevos.getNombre()); 
        user.setApellidoPaterno(datosNuevos.getApellidoPaterno()); 
        user.setApellidoMaterno(datosNuevos.getApellidoMaterno()); 
        user.setEmail(datosNuevos.getEmail()); 
        user.setRol(datosNuevos.getRol()); 
        if (datosNuevos.getContrasena() != null && !datosNuevos.getContrasena().isBlank()) {
            user.setContrasena(encriptador.encode(datosNuevos.getContrasena())); 
        }
        return repositorio.save(user); 
    }
}


