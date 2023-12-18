package com.example.danhom1.Model.Role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role findByName(RoleName name);
    @Modifying(clearAutomatically = true)
    @Transactional
    @Query(value = "INSERT INTO role(id, name) VALUES(1, 'ROLE_ADMIN') ;", nativeQuery = true)
    void init();
}
