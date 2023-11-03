package com.example.danhom1.UserStorage;

import com.example.danhom1.Storage.Storage;
import com.example.danhom1.User.User;
import jakarta.persistence.*;
import lombok.*;

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
    private Integer id;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userID",referencedColumnName = "id", nullable = false, unique = true)
    private final User user = new User();
}
