package com.example.danhom1.UserStorage;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class UserStorageService {
    private final UserStorageRepo userStorageRepo;
}
