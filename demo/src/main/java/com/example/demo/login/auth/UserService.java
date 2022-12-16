package com.example.demo.login.auth;

import com.example.demo.login.registration.token.ConfirmationToken;
import com.example.demo.login.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.example.demo.login.auth.UserRole.ADMIN;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    @Autowired
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository
                .findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("User with email %s not found", email)));
    }

    public String signUpUser(User user) {
        boolean userExits = userRepository.findByEmail(user.getEmail()).isPresent();

        if (userExits){
            throw new IllegalStateException("Email already taken");
        }
        String encoderPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encoderPassword);
        userRepository.save(user);

        String token = UUID.randomUUID().toString();
        ConfirmationToken confirmationToken = new ConfirmationToken(
                token, LocalDateTime.now(), LocalDateTime.now().plusMinutes(10),
                user
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);
        return token;
    }

    public int enableUser(String email){
        return userRepository.enableAppUser(email);
    }
}
