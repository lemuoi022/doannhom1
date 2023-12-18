package com.example.danhom1.Model.Storage;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;

// import org.springframework.beans.factory.annotation.Autowired;
import com.example.danhom1.Exception.StorageException;
import com.example.danhom1.Exception.StorageFileNotFoundException;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import javax.naming.SizeLimitExceededException;

@Service
//@Transactional
public class StorageService {
    @Setter
    @Getter
    private Path rootPath;
    private final Long remainingSpace;

    // @Autowired
    @SneakyThrows
    public StorageService(@NonNull @Autowired Storage storage) {
        // Missing separated individual user folder feature
        if(storage.getPPath().isBlank()){
            throw new StorageException("Root path is empty!");
        }
        this.rootPath = Paths.get(storage.getPPath().strip());
        if (!this.rootPath.toFile().exists())
            init(this.rootPath);
        this.remainingSpace = storage.getLimit() * 1024 * 1024 - FileUtils.sizeOfDirectory(this.rootPath.toFile());
        if (this.remainingSpace <= 0)
            throw new SizeLimitExceededException("The storage has exceed the limit!");
    }

    public void store(@NonNull MultipartFile file) throws SizeLimitExceededException {
        // Missing separated individual user folder feature
        try {
            Path toStoreFile = this.rootPath.resolve(Paths.get(Objects.requireNonNull(file.getOriginalFilename()))).normalize().toAbsolutePath();
            if (!toStoreFile.getParent().equals(this.rootPath.toAbsolutePath())) {
                throw new StorageException("Cannot store file outside current directory."
                + "\n" + toStoreFile + "\n" + this.rootPath.toAbsolutePath() + file.getOriginalFilename());
            }
            // noinspection DataFlowIssue
            toStoreFile = toNewFileIfExists(toStoreFile, Objects.requireNonNull(file.getOriginalFilename()));
            // Paths.get(file.getOriginalFilename()).normalize().toAbsolutePath();
            try (InputStream stream = file.getInputStream()) {
                if (file.getSize() > this.remainingSpace) {
                    stream.close();
                    throw new SizeLimitExceededException("The file exceeded the storage limit!");
                } else {
                    Files.copy(stream, toStoreFile);
                }
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

    public Resource loadAsResource(String filename) throws StorageException{
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

    public void init(Path rootPath) {
		try {
			Files.createDirectories(rootPath);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

    @Contract("_, _ -> param1")
    private @NonNull Path toNewFileIfExists(@NonNull Path path, @NonNull String filename){
        File file = path.toFile();
        StringBuilder str = new StringBuilder(filename);
        if (file.exists()) {
            if (!FilenameUtils.getName(filename).matches("(_[02-9]\\d*$)")) {
                if (filename.contains("."))
                    filename = str.insert(filename.lastIndexOf("."), "_1").toString();
                else filename += "_1";
                file = this.rootPath.resolve(filename).toFile();
            }
            if (file.exists()) {
                str = new StringBuilder(file.getName());
                for (int i = 2; file.exists(); i++) {
                    filename = str.replace(filename.lastIndexOf("_") + 1, filename.lastIndexOf("_") + 1 + String.valueOf(i - 1).length(), String.valueOf(i)).toString();
                    file = this.rootPath.resolve(filename).toFile();
                }
            }
        }
        return file.toPath();
    }
}