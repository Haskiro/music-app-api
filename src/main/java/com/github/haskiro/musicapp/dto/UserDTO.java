package com.github.haskiro.musicapp.dto;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class UserDTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email")
    @Email(message = "Email must match patter email@example.com")
    @NotEmpty(message = "Email must not be empty")
    private String email;

    @Column(name = "first_name")
    @Size(min = 2, max = 30, message = "First name length must be between 2 and 30 characters")
    @NotEmpty(message = "First name must not be empty")
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 2, max = 30, message = "Last name length must be between 2 and 30 characters")
    @NotEmpty(message = "Last name must not be empty")
    private String lastName;

    @Column(name = "photo")
    private String photo;

    @Column(name = "bio")
    private String bio;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }
}
