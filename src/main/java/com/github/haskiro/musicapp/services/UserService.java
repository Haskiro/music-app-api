package com.github.haskiro.musicapp.services;

import com.github.haskiro.musicapp.config.jwt.JwtService;
import com.github.haskiro.musicapp.dto.userDTO.LoginDTO;
import com.github.haskiro.musicapp.models.Role;
import com.github.haskiro.musicapp.models.User;
import com.github.haskiro.musicapp.repositories.UserRepository;
import com.github.haskiro.musicapp.security.UserDetailsImpl;
import com.github.haskiro.musicapp.util.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public AuthenticationResponse register(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setCreatedAt(OffsetDateTime.now());
        user.setRole(Role.ROLE_USER);

        userRepository.save(user);

        String jwtToken = jwtService.generateToken(new UserDetailsImpl(user));
        return new AuthenticationResponse(jwtToken);
    }

    @Transactional
    public AuthenticationResponse login(LoginDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        String jwtToken = jwtService.generateToken(new UserDetailsImpl(user));
        return new AuthenticationResponse(jwtToken);
    }
}
