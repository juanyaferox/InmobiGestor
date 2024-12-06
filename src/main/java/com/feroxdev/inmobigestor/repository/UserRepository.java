package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Encuentra un usuario por su nombre de usuario
     * @param user Nombre de usuario
     * @return Usuario
     */
    Optional<User> findByUser(String user);

    /**
     * Encuentra un usuario por su nombre, primer apellido, segundo apellido y dni
     * Objetivo: Encontrar los usuarios con los mismos datos personales para el caso modifacacion de admin
     * y asi modificarlo
     * @param dni Dni del usuario
     * @return Lista de usuarios con el mismo dni
     */
    List<User> findByDni(String dni);
}
