package com.Batman.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.User;

public interface IUserRepository extends JpaRepository<User, Integer>{
     Optional<User> findByEmail(String email);
}
