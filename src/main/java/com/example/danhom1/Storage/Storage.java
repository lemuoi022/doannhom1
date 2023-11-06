package com.example.danhom1.Storage;

import jakarta.annotation.PostConstruct;
import lombok.*;
import org.jetbrains.annotations.Contract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

// import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Objects;

@Component
@PropertySource("classpath:application.properties")
@NoArgsConstructor
public class Storage {
    private transient Environment env;

    @NonNull
    @Setter
    @Getter
    @Value("${storage.PPath}")
    private String pPath;

//    private final String vPath = "0:/";

    //limit in MB
    @NonNull
    @Getter
    private Long limit;

    @Contract(pure = true)
    @Autowired
    public Storage(Environment env){
        this.env = env;
    }

    @PostConstruct
    public void initLimit() {
        this.limit = Long.valueOf(Objects.requireNonNull(Objects.requireNonNull(this.env.getProperty("spring.servlet.multipart.max-file-size")).strip().replaceAll("[^0-9]","")));
    }
}
