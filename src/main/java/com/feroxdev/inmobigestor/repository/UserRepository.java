package com.feroxdev.inmobigestor.repository;

import com.feroxdev.inmobigestor.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUser(String user);
}
