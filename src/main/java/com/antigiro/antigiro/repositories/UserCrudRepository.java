package com.antigiro.antigiro.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.antigiro.antigiro.models.UserCrud;


public interface UserCrudRepository extends JpaRepository<UserCrud, Long> { 

}