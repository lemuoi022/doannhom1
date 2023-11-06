package com.example.danhom1.UserFile;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;
import org.apache.commons.io.FilenameUtils;
import org.jetbrains.annotations.Contract;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_file")
public class UserFile {
    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NonNull
    @Column(name = "name", nullable = false)
    @NotEmpty
    private String filename;

    @Column(name = "ext")
    private String extension;

    @NonNull
    @Column(name = "create_time", nullable = false)
    @NotEmpty
    private String createTime;

    @Contract(pure = true)
    @PostConstruct
    private void initExtension() {
        if (this.filename.contains("."))
            this.extension = FilenameUtils.getExtension(this.filename);
        else this.extension = null;
    }
}
