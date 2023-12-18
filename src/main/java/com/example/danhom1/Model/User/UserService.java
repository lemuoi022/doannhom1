package com.example.danhom1.Model.User;

import com.example.danhom1.Exception.UserAlreadyExistedException;
import com.example.danhom1.Model.Role.RoleName;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
public class UserService implements UserDetailsService {
    private final UserRepo userRepo;
//    private final UserStorageRepo userStorageRepo;
    public void RegisterNewUser(@NonNull User user) throws UserAlreadyExistedException {
        if (EmailExists((user.getEmail())))
            throw new UserAlreadyExistedException("The email " + user.getEmail() + " is already taken!");
//        UserStorage userStorage = new UserStorage();
//        userStorage.setUser(user);
//        userStorageRepo.save(userStorage);
        userRepo.save(user);
    }

    private Boolean EmailExists(String email){
        return !userRepo.findAllByEmail(email).isEmpty();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        if (!EmailExists(email))
            throw new UsernameNotFoundException("User with the email " + email + " wasn't found.");
        User user = userRepo.findUserByEmail(email);
//        Set<GrantedAuthority> authorities = user.getRole().stream()
//                .map((role) -> new SimpleGrantedAuthority(role.getName().toString())).collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPass(), AuthorityUtils.createAuthorityList("ROLE_USER"));
    }
}
