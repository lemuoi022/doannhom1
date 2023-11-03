package com.example.danhom1.User;

import com.example.danhom1.Exception.UserAlreadyExistsException;
import com.example.danhom1.UserStorage.UserStorage;
import com.example.danhom1.UserStorage.UserStorageRepo;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserService {
    private final UserRepo userRepo;
    private final UserStorageRepo userStorageRepo;
    public User RegisterNewUser(@NonNull User user) throws UserAlreadyExistsException {
        if (EmailExists((user.getEmail())))
            throw new UserAlreadyExistsException();
        UserStorage userStorage = new UserStorage();
        userStorage.setUser(user);
        userStorageRepo.save(userStorage);
        return userRepo.save(user);
    }

    private Boolean EmailExists(String email){
        return !userRepo.findAllByEmailIs(email).isEmpty();
    }
}
