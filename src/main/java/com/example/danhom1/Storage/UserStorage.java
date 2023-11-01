package com.example.danhom1.Storage;

import com.example.danhom1.User.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Entity
public class UserStorage extends Storage {

    @Id
    @NonNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Setter
    @NonNull
    @OneToOne
    @JoinColumn(name = "userID")
    private User user = new User();
}
