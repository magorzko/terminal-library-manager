package pl.edu.wszib.librarymanager.repository;

import org.springframework.stereotype.Repository;
import pl.edu.wszib.librarymanager.model.Role;
import pl.edu.wszib.librarymanager.model.User;
import pl.edu.wszib.librarymanager.security.IPasswordHasher;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class InMemoryUserRepository implements IUserRepository {

    private final List<User> users = new ArrayList<>();

    public InMemoryUserRepository(IPasswordHasher hasher) {
        // seed użytkowników: admin/admin oraz user/user
        save(User.builder()
                .username("admin")
                .salt(hasher.newSalt())
                .role(Role.ADMIN)
                .build());
        users.get(0).setPasswordHash(hasher.hash("admin", users.get(0).getSalt()));

        save(User.builder()
                .username("user")
                .salt(hasher.newSalt())
                .role(Role.USER)
                .build());
        users.get(1).setPasswordHash(hasher.hash("user", users.get(1).getSalt()));
    }

    @Override
    public Optional<User> findByUsername(String username) {
        if (username == null) {
            return Optional.empty();
        }
        String u = username.trim().toLowerCase();
        return users.stream()
                .filter(x -> x.getUsername().toLowerCase().equals(u))
                .findFirst()
                .map(this::copy);
    }

    @Override
    public void save(User user) {
        users.removeIf(u -> u.getUsername().equalsIgnoreCase(user.getUsername()));
        users.add(copy(user));
    }

    private User copy(User u) {
        return User.builder()
                .username(u.getUsername())
                .passwordHash(u.getPasswordHash())
                .salt(u.getSalt())
                .role(u.getRole())
                .build();
    }
}
