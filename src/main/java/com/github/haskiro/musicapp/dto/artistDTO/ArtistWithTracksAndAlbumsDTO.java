package com.github.haskiro.musicapp.dto.artistDTO;

import com.github.haskiro.musicapp.dto.albumDTO.AlbumDTO;
import com.github.haskiro.musicapp.dto.trackDTO.TrackDTO;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.Set;

public class ArtistWithTracksAndAlbumsDTO {
    private int id;

    @Size(min = 2, max = 30, message = "Nickname length must be between 2 and 30 characters")
    @NotEmpty(message = "Nickname must not be empty")
    private String nickname;

    @Size(min = 2, max = 30, message = "First name length must be between 2 and 30 characters")
    @NotEmpty(message = "First name must not be empty")
    private String firstName;

    @Size(min = 2, max = 30, message = "Last name length must be between 2 and 30 characters")
    @NotEmpty(message = "Last name must not be empty")
    private String lastName;

    private LocalDate birthDate;

    private String photo;

    private String bio;

    private Set<TrackDTO> tracks;

    private Set<AlbumDTO> albums;

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

    public Set<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(Set<TrackDTO> tracks) {
        this.tracks = tracks;
    }

    public Set<AlbumDTO> getAlbums() {
        return albums;
    }

    public void setAlbums(Set<AlbumDTO> albums) {
        this.albums = albums;
    }
}
