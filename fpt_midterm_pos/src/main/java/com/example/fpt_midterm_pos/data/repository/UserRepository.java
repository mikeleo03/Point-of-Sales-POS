package com.example.fpt_midterm_pos.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fpt_midterm_pos.data.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    // Find User based on his/her username
    User findByUsername(String username);
}
