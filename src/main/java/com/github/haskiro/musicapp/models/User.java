package com.github.haskiro.musicapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Objects;

@Entity
@Table(name = "\"user\"")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", unique = true)
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

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime createdAt;

    @Column(name = "password")
    private String password;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    private Role role;

    public User(String email, String firstName, String lastName, String photo, String bio, String password) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.photo = photo;
        this.bio = bio;
        this.password = password;
    }

    public User() {
    }

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

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId() == user.getId() && Objects.equals(getEmail(), user.getEmail()) && Objects.equals(getFirstName(), user.getFirstName()) && Objects.equals(getLastName(), user.getLastName()) && Objects.equals(getPhoto(), user.getPhoto()) && Objects.equals(getBio(), user.getBio()) && Objects.equals(getBirthDate(), user.getBirthDate()) && Objects.equals(getCreatedAt(), user.getCreatedAt()) && Objects.equals(getPassword(), user.getPassword());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail(), getFirstName(), getLastName(), getPhoto(), getBio(), getBirthDate(), getCreatedAt(), getPassword());
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", photo='" + photo + '\'' +
                ", bio='" + bio + '\'' +
                ", birthDate=" + birthDate +
                ", createdAt=" + createdAt +
                '}';
    }
}
