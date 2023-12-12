package com.example.danhom1.Model.UserStorage;

import com.example.danhom1.Model.User.User;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserStorageService {
    private final UserStorageRepo userStorageRepo;
    public void RegisterNewUser(@NonNull User user) {
        UserStorage userStorage = new UserStorage();
        userStorage.setUser(user);
        userStorageRepo.save(userStorage);
    }
}
