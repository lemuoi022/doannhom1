package com.example.danhom1.UserFile;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Transactional
public class UserFileService {
    private final UserFileRepo userFileRepo;
}
