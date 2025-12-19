package pl.edu.wszib.librarymanager.service;

import pl.edu.wszib.librarymanager.model.User;

import java.util.Optional;

public interface IAuthService {
    Optional<User> login(String username, String password);
}
