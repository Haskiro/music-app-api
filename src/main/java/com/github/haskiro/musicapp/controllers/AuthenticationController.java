package com.github.haskiro.musicapp.controllers;


import com.github.haskiro.musicapp.dto.userDTO.LoginDTO;
import com.github.haskiro.musicapp.dto.userDTO.RegistrationDTO;
import com.github.haskiro.musicapp.dto.userDTO.UserDTO;
import com.github.haskiro.musicapp.models.User;
import com.github.haskiro.musicapp.services.UserService;
import com.github.haskiro.musicapp.util.AuthenticationResponse;
import com.github.haskiro.musicapp.util.ErrorResponse;
import com.github.haskiro.musicapp.util.exceptions.UserRegisterException;
import com.github.haskiro.musicapp.util.UserValidator;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

import static com.github.haskiro.musicapp.util.ErrorUtil.returnErrorsAsString;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final UserValidator userValidator;

    @Autowired
    public AuthenticationController(UserService userService, ModelMapper modelMapper, UserValidator userValidator) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.userValidator = userValidator;
    }

    @PostMapping("/registration")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody @Valid RegistrationDTO request,
                                                           BindingResult bindingResult) {
        User user = convertToUser(request);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = returnErrorsAsString(bindingResult);

            throw new UserRegisterException(errorMessage);
        }


        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid LoginDTO request,
                                                        BindingResult bindingResult) {
        return ResponseEntity.ok(userService.login(request));
    }

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }
    public User convertToUser(RegistrationDTO registrationDTO) {
        return modelMapper.map(registrationDTO, User.class);
    }
    public User convertToUser(LoginDTO loginDTO) {
        return modelMapper.map(loginDTO, User.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserRegisterException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AuthenticationException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
    }

}
