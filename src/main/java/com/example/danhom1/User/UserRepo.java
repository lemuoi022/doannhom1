package com.example.danhom1.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findAllByEmailIs(String email);
}
