package com.example.danhom1.Model.UserDTO;

import com.example.danhom1.Validator.Annotation.PasswordsMatch;
import com.example.danhom1.Validator.Annotation.ValidPassword;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;

@PasswordsMatch.List(@PasswordsMatch(
        field = "password",
        fieldMatch = "confirmPassword",
        message = "The passwords don't match each other."
))
@Data
public class UserRegisterDto {
    @NonNull
    @NotEmpty
    private String username;

    @NonNull
    @NotEmpty
    @ValidPassword(message = "The password is invalid.")
    private String password;

    @NonNull
    @NotEmpty
    @ValidPassword
    private String confirmPassword;

    @NonNull
    @NotEmpty
    @Email(message = "The email is invalid.")
    private String email;
}
