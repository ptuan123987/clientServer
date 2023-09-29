package com.example.clientServer.repository;

import com.example.clientServer.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String sender);
}
