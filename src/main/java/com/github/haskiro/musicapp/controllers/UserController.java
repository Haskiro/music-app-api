package com.github.haskiro.musicapp.controllers;


import com.github.haskiro.musicapp.dto.userDTO.UserDTO;
import com.github.haskiro.musicapp.models.User;
import com.github.haskiro.musicapp.services.UserService;
import com.github.haskiro.musicapp.util.*;
import com.github.haskiro.musicapp.util.exceptions.UserNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") int id) {
        return convertToUserDTO(userService.findById(id));
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "User not found",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

}
