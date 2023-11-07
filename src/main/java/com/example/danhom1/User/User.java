package com.example.danhom1.User;

import com.example.danhom1.UserStorage.UserStorage;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Builder
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
    @Column(name = "name", nullable = false)
    @NotEmpty
    private String name;

    @NonNull
    @Column(name = "pass", nullable = false, unique = true, length = 24)
    @NotEmpty
    private String pass;

    @Column(name = "email", nullable = false, unique = true)
    @NonNull
    @NotEmpty
    @Email
    private String email;

    @OneToOne(mappedBy = "user")
    private UserStorage userStorage;
}
