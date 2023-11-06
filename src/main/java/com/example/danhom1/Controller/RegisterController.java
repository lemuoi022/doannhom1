package com.example.danhom1.Controller;

import com.example.danhom1.User.UserService;
import com.example.danhom1.UserFile.UserFileService;
import com.example.danhom1.UserStorage.UserStorageService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;

@Controller
@AllArgsConstructor
public class RegisterController {
    private final UserService userService;
    private final UserFileService userFileService;
    private final UserStorageService userStorageService;
}
