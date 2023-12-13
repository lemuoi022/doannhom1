package com.example.danhom1.Controller;

import com.example.danhom1.Model.ResponseMessage;
import com.example.danhom1.Model.UserDTO.UserLoginDto;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@AllArgsConstructor
public class LoginController {
    private AuthenticationManager authenticationManager;
    private PasswordEncoder passwordEncoder;
    @PostMapping("/login")
    public ResponseEntity<ResponseMessage> login (@RequestBody @NotNull UserLoginDto userLoginDto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(userLoginDto.getEmail(), userLoginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(new ResponseMessage("User logged in successfully."));
    }
}
