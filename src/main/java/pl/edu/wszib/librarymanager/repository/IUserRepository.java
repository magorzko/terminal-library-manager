package pl.edu.wszib.librarymanager.repository;

import pl.edu.wszib.librarymanager.model.User;

import java.util.Optional;

public interface IUserRepository {
    Optional<User> findByUsername(String username);
    void save(User user);
}
