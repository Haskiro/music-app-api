package com.github.haskiro.musicapp.controllers;


import com.github.haskiro.musicapp.dto.UserDTO;
import com.github.haskiro.musicapp.models.User;
import com.github.haskiro.musicapp.services.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@Controller
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
