package com.example.danhom1.Model.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepo extends JpaRepository<User, Long> {
    List<User> findAllByEmail(String email);
    User findUserByEmail(String email);
}
