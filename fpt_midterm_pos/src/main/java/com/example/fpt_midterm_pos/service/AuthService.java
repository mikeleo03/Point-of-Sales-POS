package com.example.fpt_midterm_pos.service;

import com.example.fpt_midterm_pos.data.model.User;
import com.example.fpt_midterm_pos.exception.AuthException;

public interface AuthService {

    // Find user by its username
    User findByUsername(String username);

    // Authenticate user based on given user data
    void authenticate(String username, String password) throws AuthException;

    // Save the user to the database
    void saveUser(User user);
}
