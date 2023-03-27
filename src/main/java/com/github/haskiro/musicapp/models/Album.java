package com.github.haskiro.musicapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "album")
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @NotEmpty(message = "Title must not be empty")
    private String title;

    @Column(name = "description", columnDefinition = "text")
    private String description;

    @Column(name = "cover")
    private String cover;

    @Column(name = "created_at")
    @NotNull
    private OffsetDateTime createdAt;

    @ManyToMany
    @JoinTable(name = "album_track",
    joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "track_id", referencedColumnName = "id"))
    Set<Track> trackList;

    @ManyToMany
    @JoinTable(name = "album_artist",
            joinColumns = @JoinColumn(name = "album_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id", referencedColumnName = "id"))
    Set<Artist> artistList;

    public Album() {
    }

    public Album(String title, String description, String cover) {
        this.title = title;
        this.description = description;
        this.cover = cover;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public Set<Track> getTrackList() {
        return trackList;
    }

    public void setTrackList(Set<Track> trackList) {
        this.trackList = trackList;
    }

    public Set<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(Set<Artist> artistList) {
        this.artistList = artistList;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return getId() == album.getId() && Objects.equals(getTitle(), album.getTitle()) && Objects.equals(getDescription(), album.getDescription()) && Objects.equals(getCover(), album.getCover()) && Objects.equals(getCreatedAt(), album.getCreatedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getDescription(), getCover(), getCreatedAt());
    }

    @Override
    public String toString() {
        return "Album{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", cover='" + cover + '\'' +
                '}';
    }
}
