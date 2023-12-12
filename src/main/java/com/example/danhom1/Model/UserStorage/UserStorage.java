package com.example.danhom1.Model.UserStorage;

import com.example.danhom1.Model.Storage.Storage;
import com.example.danhom1.Model.User.User;
import com.example.danhom1.Model.UserFile.UserFile;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_storage")
public class UserStorage extends Storage {
    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userID",referencedColumnName = "id", nullable = false, unique = true)
    private User user;

    @NonNull
    @OneToMany(mappedBy = "userStorage", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<UserFile> userFiles;
}
