package com.example.demo.login.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenReposity;

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenReposity.save(token);
    }
    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenReposity.findByToken(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenReposity.updateConfirmedAt(
                token, LocalDateTime.now());
    }
}
