package com.event.system.service;

import com.event.system.dto.AuthRequest;
import com.event.system.dto.AuthResponse;
import com.event.system.model.User;
import com.event.system.repository.UserRepository;
import com.event.system.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public User register(AuthRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already exists");
        }
        if (request.getEmail() != null && userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        User user = User.builder()
                .name(request.getName() != null ? request.getName() : request.getUsername())
                .username(request.getUsername())
                .email(request.getEmail() != null ? request.getEmail() : request.getUsername() + "@college.edu")
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole() != null ? request.getRole() : User.Role.ORGANIZER)
                .build();

        return userRepository.save(user);
    }

    public AuthResponse login(AuthRequest request) {
        Optional<User> userOpt = userRepository.findByUsername(request.getUsername());
        if (userOpt.isEmpty() || !passwordEncoder.matches(request.getPassword(), userOpt.get().getPassword())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username or password");
        }

        User user = userOpt.get();
        String token = jwtTokenUtil.generateToken(user.getUsername(), user.getRole().name());

        return new AuthResponse(token, user.getRole().name(), user.getUsername());
    }
}
