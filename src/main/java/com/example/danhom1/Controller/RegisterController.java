package com.example.danhom1.Controller;

import com.example.danhom1.Model.ResponseMessage;
import com.example.danhom1.Model.User.User;
import com.example.danhom1.Model.User.UserService;
import com.example.danhom1.Model.UserDTO.UserRegisterDto;
import com.example.danhom1.Model.UserStorage.UserStorageService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class RegisterController {
    private final UserService userService;
    private final UserStorageService userStorageService;
    private final PasswordEncoder passwordEncoder;
    @PostMapping("/register")
    public ResponseEntity<ResponseMessage> registerUser(@RequestBody @Valid @NotNull UserRegisterDto userRegisterDto) {
        User user = new User();
        user.setName(userRegisterDto.getUsername());
        user.setPass(passwordEncoder.encode(userRegisterDto.getPassword()));
        user.setEmail(userRegisterDto.getEmail());
        try {
            userService.RegisterNewUser(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(e.getMessage()));
        }
        userStorageService.RegisterNewUser(user);
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("User registered successfully."));
    }
}
