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
    @Column(name = "id", nullable = false)
    private Integer id;

    @NonNull
    @NotEmpty
    @Column(name = "userLimit", nullable = false)
    private Float userLimit = super.getDefaultLimit();

    @NonNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userID",referencedColumnName = "id")
    private User user = new User();
}
