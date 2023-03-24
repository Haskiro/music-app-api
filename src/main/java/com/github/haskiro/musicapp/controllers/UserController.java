package com.github.haskiro.musicapp.controllers;


import com.github.haskiro.musicapp.dto.userDTO.RegistrationDTO;
import com.github.haskiro.musicapp.dto.userDTO.UserDTO;
import com.github.haskiro.musicapp.models.User;
import com.github.haskiro.musicapp.services.UserService;
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
    private final UserValidator userValidator;

    @Autowired
    public UserController(UserService userService, ModelMapper modelMapper, UserValidator userValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    @GetMapping
    public List<UserDTO> getUsers() {
        List<User> users = userService.findAll();
        return users.stream().map(this::convertToUserDTO)
                .collect(Collectors.toList());
    }

    @PostMapping("/registration")
    public ResponseEntity<HttpStatus> register(@RequestBody @Valid RegistrationDTO registrationDTO,
                                               BindingResult bindingResult) {
        User user = convertToUser(registrationDTO);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = returnErrorsAsString(bindingResult);

            throw new UserRegistrationError(errorMessage);
        }

        userService.register(user);

        return ResponseEntity.ok(HttpStatus.OK);


    }

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
    public User convertToUser(RegistrationDTO registrationDTO) {
        return modelMapper.map(registrationDTO, User.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserRegistrationError e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}
