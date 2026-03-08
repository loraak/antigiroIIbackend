package com.antigiro.antigiro.services;

import com.antigiro.antigiro.models.User;
import com.antigiro.antigiro.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository UserRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User User = UserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User no encontrado: " + email));
        
        return org.springframework.security.core.userdetails.User
                .withUsername(User.getEmail())
                .password(User.getContrasena())
                .authorities("USER")
                .build();
    }
    
    public User loadUserByEmail(String email) {
        return UserRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User no encontrado: " + email));
    }
}