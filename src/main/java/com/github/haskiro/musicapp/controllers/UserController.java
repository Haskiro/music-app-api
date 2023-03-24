package com.github.haskiro.musicapp.controllers;


import com.github.haskiro.musicapp.dto.userDTO.LoginDTO;
import com.github.haskiro.musicapp.dto.userDTO.RegistrationDTO;
import com.github.haskiro.musicapp.dto.userDTO.UserDTO;
import com.github.haskiro.musicapp.models.User;
import com.github.haskiro.musicapp.services.UserService;
import com.github.haskiro.musicapp.util.AuthenticationResponse;
import com.github.haskiro.musicapp.util.ErrorResponse;
import com.github.haskiro.musicapp.util.UserRegistrationError;
import com.github.haskiro.musicapp.util.UserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.haskiro.musicapp.util.ErrorUtil.returnErrorsAsString;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        List<User> users = userService.findAll();
        return users.stream().map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

}
