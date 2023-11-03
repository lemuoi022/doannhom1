package com.example.danhom1.User;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepo userRepo;
    public User RegisterNewUser(User user) throws UserAlreadyExistException {
        if (EmailExists((user.getEmail())))
            throw new UserAlreadyExistException();
        return userRepo.save(user);
    }

    private Boolean EmailExists(String email){
        return !userRepo.findAllByEmailIs(email).isEmpty();
    }
}
