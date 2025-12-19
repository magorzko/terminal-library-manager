package pl.edu.wszib.librarymanager.security;

public interface IPasswordHasher {
    String newSalt();
    String hash(String password, String salt);
    boolean matches(String password, String salt, String expectedHash);
}
