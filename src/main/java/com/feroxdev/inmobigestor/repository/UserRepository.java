package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUser(String user);
}
