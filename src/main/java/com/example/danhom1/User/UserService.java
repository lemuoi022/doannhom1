package com.example.danhom1.User;

import com.example.danhom1.Exception.UserAlreadyExistException;
import com.example.danhom1.UserStorage.UserStorage;
import com.example.danhom1.UserStorage.UserStorageRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepo userRepo;
    private final UserStorageRepo userStorageRepo;
    public User RegisterNewUser(User user) throws UserAlreadyExistException {
        if (EmailExists((user.getEmail())))
            throw new UserAlreadyExistException();
        UserStorage userStorage = new UserStorage();
        userStorage.setUser(user);
        userStorageRepo.save(userStorage);
        return userRepo.save(user);
    }

    private Boolean EmailExists(String email){
        return !userRepo.findAllByEmailIs(email).isEmpty();
    }
}
