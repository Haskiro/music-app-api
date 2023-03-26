package com.github.haskiro.musicapp.controllers;


import com.github.haskiro.musicapp.dto.userDTO.PasswordDTO;
import com.github.haskiro.musicapp.dto.userDTO.RoleDTO;
import com.github.haskiro.musicapp.dto.userDTO.UserDTO;
import com.github.haskiro.musicapp.models.Role;
import com.github.haskiro.musicapp.models.User;
import com.github.haskiro.musicapp.services.UserService;
import com.github.haskiro.musicapp.util.*;
import com.github.haskiro.musicapp.util.exceptions.UserNotFoundException;
import com.github.haskiro.musicapp.util.exceptions.UserCreateUpdateException;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    @GetMapping("/{id}")
    public UserDTO getUser(@PathVariable("id") int id) {
        return convertToUserDTO(userService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable("id") int id) {
        userService.deleteUser(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateUser(@PathVariable("id") int id,
                                                 @RequestBody @Valid UserDTO request,
                                                 BindingResult bindingResult) {
        User user = convertToUser(request);
        user.setId(id);
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            String errorMessage = returnErrorsAsString(bindingResult);

            throw new UserCreateUpdateException(errorMessage);
        }

        userService.updateUser(user);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/set-role")
    public ResponseEntity<HttpStatus> setRole(@PathVariable("id") int id,
                                              @RequestBody RoleDTO roleDTO) {
        userService.setRole(id, roleDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}/change-password")
    public ResponseEntity<HttpStatus> changePassword(@PathVariable("id") int id,
                                              @RequestBody PasswordDTO passwordDTO) {
        userService.changePassword(id, passwordDTO);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    public User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "User not found",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(UserCreateUpdateException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
