package com.example.danhom1.Model.UserDTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import lombok.NonNull;

@Data
public class UserLoginDto {
    @NonNull
    @NotEmpty
    @Email
    private String email;

    @NonNull
    @NotEmpty
    private String password;
}
