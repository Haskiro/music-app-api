package com.github.haskiro.musicapp.services;

import com.github.haskiro.musicapp.config.jwt.JwtService;
import com.github.haskiro.musicapp.dto.userDTO.LoginDTO;
import com.github.haskiro.musicapp.dto.userDTO.PasswordDTO;
import com.github.haskiro.musicapp.dto.userDTO.RoleDTO;
import com.github.haskiro.musicapp.models.Role;
import com.github.haskiro.musicapp.models.User;
import com.github.haskiro.musicapp.repositories.UserRepository;
import com.github.haskiro.musicapp.security.UserDetailsImpl;
import com.github.haskiro.musicapp.util.AuthenticationResponse;
import com.github.haskiro.musicapp.util.exceptions.FileUploadException;
import com.github.haskiro.musicapp.util.exceptions.UserCreateUpdateException;
import com.github.haskiro.musicapp.util.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(int id) {
        return userRepository.findById(id)
                .orElseThrow(UserNotFoundException::new);
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
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found!"));

        authenticationManager.authenticate (
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String jwtToken = jwtService.generateToken(new UserDetailsImpl(user));
        return new AuthenticationResponse(jwtToken);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void deleteUser(int id) {
        User user = findById(id);

        userRepository.delete(user);
    }

    @Transactional
    public void updateUser(User userToBeUpdated) {
        User user = findById(userToBeUpdated.getId());

        userToBeUpdated.setCreatedAt(user.getCreatedAt());
        userToBeUpdated.setRole(user.getRole());
        userToBeUpdated.setPassword(user.getPassword());

        userRepository.save(userToBeUpdated);
    }

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setRole(int id, RoleDTO roleDTO) {
        try {
           Role role = Role.valueOf(roleDTO.getName());
            User user = findById(id);
            user.setRole(role);

        } catch (IllegalArgumentException ex) {
            throw new UserCreateUpdateException("Role not found");
        }
    }

    @Transactional
    public void changePassword(int id, PasswordDTO passwordDTO) {
        User user = findById(id);

        String encodedPassword = passwordEncoder.encode(passwordDTO.getPassword());
        user.setPassword(encodedPassword);
    }

    @Transactional
    public void setPhoto(int id, MultipartFile file) {
        String fileUri =  uploadPath + "/users/photo/" + UUID.randomUUID() + file.getOriginalFilename();

        saveFile(fileUri, Paths.get(uploadPath + "/users/photo/"), file);

        User user = findById(id);
        user.setPhoto(fileUri);
    }

    private void saveFile(String fileUri, Path fileUrl, MultipartFile multipartFile)  {
        try {
            Files.createDirectories(fileUrl);
            File file = new File(fileUri);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new FileUploadException();
        }
    }
}
