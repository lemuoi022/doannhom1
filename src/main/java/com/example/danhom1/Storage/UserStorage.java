package com.example.danhom1.Storage;

import com.example.danhom1.User.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
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
    @NotEmpty(message = "There must be a default limit.")
    @Column(name = "userLimit", nullable = false)
    private Float userLimit = super.getDefaultLimit();

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userID",referencedColumnName = "id", nullable = false, unique = true)
    private final User user = new User();

    @NonNull
    @NotEmpty(message = "There must be a root path.")
    @Column(name = "rootPath", nullable = false)
    private final String rootPath = super.getPPath() + user.getName();
}
