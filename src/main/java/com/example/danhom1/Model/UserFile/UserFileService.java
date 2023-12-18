package com.example.danhom1.Model.UserFile;

import com.example.danhom1.Model.UserStorage.UserStorage;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Path;
import java.time.LocalTime;

@Service
@AllArgsConstructor
@Transactional
public class UserFileService {
    private final UserFileRepo userFileRepo;
    public void store(Path file, UserStorage userStorage) {
        UserFile userFile = new UserFile();
        userFile.setUserStorage(userStorage);
        userFile.setFilename(file.getFileName().toString());
        userFile.initExtension();
        userFile.setCreateTime(LocalTime.now().toString());
        userFileRepo.save(userFile);
    }
}
