package com.example.danhom1.UserStorage;

import com.example.danhom1.Storage.Storage;
import com.example.danhom1.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

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
    @NotEmpty
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false, unique = true)
    private Long id;

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userID",referencedColumnName = "id", nullable = false, unique = true)
    private User user;
}
