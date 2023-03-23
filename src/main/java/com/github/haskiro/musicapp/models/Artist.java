package com.github.haskiro.musicapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "artist")
public class Artist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nickname")
    @Size(min = 2, max = 30, message = "Nickname length must be between 2 and 30 characters")
    @NotEmpty(message = "Nickname must not be empty")
    private String nickname;

    @Column(name = "first_name")
    @Size(min = 2, max = 30, message = "First name length must be between 2 and 30 characters")
    @NotEmpty(message = "First name must not be empty")
    private String firstName;

    @Column(name = "last_name")
    @Size(min = 2, max = 30, message = "Last name length must be between 2 and 30 characters")
    @NotEmpty(message = "Last name must not be empty")
    private String lastName;

    @Column(name = "birth_date")
    @Temporal(TemporalType.DATE)
    private LocalDate birthDate;

    @Column(name = "photo")
    private String photo;

    @Column(name = "bio")
    private String bio;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime createdAt;

    @ManyToMany(mappedBy = "artistList")
    List<Track> trackList;

    public Artist(String nickname, String firstName, String lastName, LocalDate birthDate, String photo, String bio, OffsetDateTime createdAt) {
        this.nickname = nickname;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.photo = photo;
        this.bio = bio;
        this.createdAt = createdAt;
    }

    public Artist() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
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

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
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

    public List<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(List<Track> trackList) {
        this.trackList = trackList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Artist artist = (Artist) o;
        return id == artist.id && Objects.equals(nickname, artist.nickname) && Objects.equals(firstName, artist.firstName) && Objects.equals(lastName, artist.lastName) && Objects.equals(birthDate, artist.birthDate) && Objects.equals(photo, artist.photo) && Objects.equals(bio, artist.bio) && Objects.equals(createdAt, artist.createdAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nickname, firstName, lastName, birthDate, photo, bio, createdAt);
    }

    @Override
    public String toString() {
        return "Artist{" +
                "id=" + id +
                ", nickname='" + nickname + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", birthDate=" + birthDate +
                ", photo='" + photo + '\'' +
                ", bio='" + bio + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
