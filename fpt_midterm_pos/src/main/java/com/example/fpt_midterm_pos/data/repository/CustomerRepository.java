package com.example.fpt_midterm_pos.data.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.fpt_midterm_pos.data.model.Customer;
@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

}
