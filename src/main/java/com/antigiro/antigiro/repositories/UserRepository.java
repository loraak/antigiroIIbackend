package com.antigiro.antigiro.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.antigiro.antigiro.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);

    public Optional<User> findByEmail(String email);

    
}
