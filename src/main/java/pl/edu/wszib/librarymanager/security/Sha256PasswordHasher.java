package pl.edu.wszib.librarymanager.security;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

@Component
public class Sha256PasswordHasher implements IPasswordHasher {

    private final SecureRandom random = new SecureRandom();

    @Override
    public String newSalt() {
        byte[] bytes = new byte[16];
        random.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }

    @Override
    public String hash(String password, String salt) {
        String input = (salt == null ? "" : salt) + ":" + (password == null ? "" : password);
        return DigestUtils.sha256Hex(input.getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public boolean matches(String password, String salt, String expectedHash) {
        if (expectedHash == null) {
            return false;
        }
        return expectedHash.equals(hash(password, salt));
    }
}
