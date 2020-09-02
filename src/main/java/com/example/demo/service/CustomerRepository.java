package com.example.demo.service;

import com.example.demo.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;


public interface CustomerRepository extends JpaRepository<Customer , Integer> {
    @Query("select c from Customer c where c.email = ?1")
    Customer findByEmail(String email);

    @Query("update Customer c set c.enabled = true where c.id = ?1")
    @Modifying
    void enable(Integer id);

    @Query("select c from Customer c where c.verification = ?1")
    Customer findByVeritification(String code);
}
