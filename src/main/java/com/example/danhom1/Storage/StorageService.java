package com.example.danhom1.Storage;

import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import java.util.stream.Stream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

// import org.springframework.beans.factory.annotation.Autowired;
import com.example.danhom1.Exception.StorageException;
import com.example.danhom1.Exception.StorageFileNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

@Service
@Transactional
public class StorageService {
    private final Path rootPath;
    // @Autowired
    public StorageService(Storage storage){
        // Missing separated individual user folder feature
        if(storage.getPPath().isBlank()){
            throw new StorageException("Root path is empty!");
        }
        this.rootPath = Paths.get(storage.getPPath().strip());
    }

    public void store(MultipartFile file) {
        // Missing separated individual user folder feature
        try {
            Path toStoreFile = this.rootPath.resolve(Paths.get(Objects.requireNonNull(file.getOriginalFilename()))).normalize().toAbsolutePath();
            if (!toStoreFile.getParent().equals(this.rootPath.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory." 
                + "\n" + toStoreFile + "\n" + this.rootPath.toAbsolutePath() + file.getOriginalFilename()
                );
            }
            // Paths.get(file.getOriginalFilename()).normalize().toAbsolutePath();
            try (InputStream stream = file.getInputStream()) {
                Files.copy(stream, 
                toStoreFile, 
                StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file.", e);
        }
    }

    public Path load(String filename){
        return rootPath.resolve(filename);
    }

    public Stream<Path> loadAll() {
        try {
            //noinspection resource
            return Files.walk(this.rootPath, 1).filter(path -> !path.equals(this.rootPath)).map(this.rootPath::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}
	}

    public Resource loadAsResource(String filename){
        try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException("Could not read file: " + filename);
			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
    }

    public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootPath.toFile());
	}

    public void init() {
		try {
			Files.createDirectories(rootPath);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}
}