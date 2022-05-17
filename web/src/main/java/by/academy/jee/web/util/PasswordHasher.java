package by.academy.jee.web.util;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PasswordHasher {

    private final PasswordEncoder passwordEncoder;

    public String getEncryptedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}