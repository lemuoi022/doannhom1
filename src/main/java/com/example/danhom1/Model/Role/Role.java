package com.example.danhom1.Model.Role;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Entity
@Component
@Table(name = "role")
@AllArgsConstructor
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Enumerated(EnumType.STRING)
    private RoleName name;

    private transient RoleRepo roleRepo;
    @Autowired
    private Role(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @PostConstruct
    private void init() {
        if (roleRepo.findByName(RoleName.ROLE_ADMIN) == null) {
            roleRepo.init();
        }
    }

    @Override
    public String getAuthority() {
        return this.getName().name();
    }
}
