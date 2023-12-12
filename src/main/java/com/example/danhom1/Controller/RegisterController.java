package com.example.danhom1.Controller;

import com.example.danhom1.Model.User.User;
import com.example.danhom1.Model.User.UserService;
import com.example.danhom1.Model.UserDTO.UserRegisterDto;
import com.example.danhom1.Model.UserStorage.UserStorageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class RegisterController {
    private final UserService userService;
    private final UserStorageService userStorageService;
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody @Valid @NotNull UserRegisterDto userRegisterDto) {
        User user = new User();
        user.setName(userRegisterDto.getUsername());
        user.setPass(userRegisterDto.getPassword());
        user.setEmail(userRegisterDto.getEmail());
        try {
            userService.RegisterNewUser(user);
        } catch (Exception e) {
            return new ResponseEntity<>("The email has already been taken!", HttpStatus.BAD_REQUEST);
        }
        userStorageService.RegisterNewUser(user);
        return new ResponseEntity<>("User registered successfully.", HttpStatus.OK);
    }
}
