package com.Batman.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Batman.entity.User;

public interface IUserRepository extends JpaRepository<User, Integer>{

}
