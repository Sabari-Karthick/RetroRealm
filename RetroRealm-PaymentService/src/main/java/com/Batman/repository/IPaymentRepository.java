package com.Batman.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.Payment;

public interface IPaymentRepository extends JpaRepository<Payment, Integer>{

}
