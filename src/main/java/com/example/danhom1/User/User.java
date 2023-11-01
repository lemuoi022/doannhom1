package com.example.danhom1.User;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    @NonNull
    private Integer id;

    @Getter
    @NonNull
    @NotEmpty(message = "Username can't not be empty")
    private String name;

    @NonNull
    @NotEmpty(message = "Password can't not be empty")
    private String pass;

    @Email(message = "Email is not valid", regexp = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")
    @Getter
    @NonNull
    @NotEmpty
    private String email;
}
