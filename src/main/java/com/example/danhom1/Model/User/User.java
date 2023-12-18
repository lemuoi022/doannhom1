package com.example.danhom1.Model.User;

import com.example.danhom1.Model.Role.Role;
import com.example.danhom1.Model.Role.RoleName;
import com.example.danhom1.Model.Role.RoleRepo;
import com.example.danhom1.Model.UserStorage.UserStorage;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false, unique = true)
    @Length(min = 2, max = 32, message = "Username must between 2 and 32 characters.")
    @Pattern(regexp = "[^<>:\"/\\\\|?*]+", message = "Forbidden username.")
    @NotEmpty
    private String name;

    @NonNull
    @Column(name = "pass", nullable = false)
    @NotEmpty
    private String pass;

    @Column(name = "email", nullable = false, unique = true)
    @NonNull
    @NotEmpty
    @Email
    private String email;

    @OneToOne(mappedBy = "user")
    private UserStorage userStorage;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(joinColumns = @JoinColumn(referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(referencedColumnName = "id"))
    private Set<Role> role;
}
