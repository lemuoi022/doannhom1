package com.example.danhom1.User;

import com.example.danhom1.UserStorage.UserStorage;
import com.example.danhom1.Validator.Annotation.PasswordsMatch;
import com.example.danhom1.Validator.Annotation.ValidEmail;
import com.example.danhom1.Validator.Annotation.ValidPassword;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
//import jakarta.validation.constraints.Pattern;
import lombok.*;

@PasswordsMatch.List({@PasswordsMatch(field = "pass", fieldMatch = "repeatPass", message = "Passwords do not match.")})
@Builder
@Data
@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @NonNull
    @NotEmpty
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false)
    @NotEmpty(message = "Username can't not be empty")
    private String name;

    //Password must have at minimum 8 characters, maximum 24 characters
    //Must include a lower character, an upper character, a number and a special character
    // @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,24}$")
    @ValidPassword
    @NonNull
    @Column(name = "pass", nullable = false, unique = true, length = 24)
    @NotEmpty(message = "Password can't not be empty")
    private String pass;

    @ValidPassword
    @NonNull
    @NotEmpty(message = "Repeated password can't not be empty.")
    transient private String repeatPass;

    @Column(name = "email", nullable = false, unique = true)
    @Email(message = "Email is not valid")
    @ValidEmail
    @NonNull
    @NotEmpty
    private String email;

    @OneToOne(mappedBy = "user")
    private UserStorage userStorage;
}
