package pl.edu.wszib.librarymanager.service;

import org.springframework.stereotype.Service;
import pl.edu.wszib.librarymanager.model.User;
import pl.edu.wszib.librarymanager.repository.IUserRepository;
import pl.edu.wszib.librarymanager.security.IPasswordHasher;

import java.util.Optional;

@Service
public class AuthService implements IAuthService {

    private final IUserRepository userRepository;
    private final IPasswordHasher passwordHasher;

    public AuthService(IUserRepository userRepository, IPasswordHasher passwordHasher) {
        this.userRepository = userRepository;
        this.passwordHasher = passwordHasher;
    }

    @Override
    public Optional<User> login(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(u -> passwordHasher.matches(password, u.getSalt(), u.getPasswordHash()));
    }
}
