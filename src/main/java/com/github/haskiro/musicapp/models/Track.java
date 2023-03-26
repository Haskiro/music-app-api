package com.github.haskiro.musicapp.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.cglib.core.Local;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "track")
public class Track {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "title")
    @Size(min = 2, max = 30, message = "Title length must be between 2 and 30 characters")
    @NotEmpty(message = "Title must not be empty")
    private String title;

    @Column(name = "cover")
    private String cover;

    @Column(name = "audio_file")
    @NotNull(message = "Audio file must not be null")
    private String audioFile;

    @Column(name = "released_at")
    @Temporal(TemporalType.DATE)
    private LocalDate releasedAt;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private OffsetDateTime createdAt;

    @ManyToMany
    @JoinTable(
            name = "artist_track",
            joinColumns = @JoinColumn(name = "track_id"),
            inverseJoinColumns = @JoinColumn(name = "artist_id")
    )
    List<Artist> artistList;

    public Track(String title, String cover, String audioFile, LocalDate releasedAt) {
        this.title = title;
        this.cover = cover;
        this.audioFile = audioFile;
        this.releasedAt = releasedAt;
    }

    public Track() {
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

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getAudioFile() {
        return audioFile;
    }

    public void setAudioFile(String audioFile) {
        this.audioFile = audioFile;
    }

    public LocalDate getReleasedAt() {
        return releasedAt;
    }

    public void setReleasedAt(LocalDate releasedAt) {
        this.releasedAt = releasedAt;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public List<Artist> getArtistList() {
        return artistList;
    }

    public void setArtistList(List<Artist> artistList) {
        this.artistList = artistList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Track track = (Track) o;
        return getId() == track.getId() && Objects.equals(getTitle(), track.getTitle()) && Objects.equals(getCover(), track.getCover()) && Objects.equals(getAudioFile(), track.getAudioFile()) && Objects.equals(getReleasedAt(), track.getReleasedAt());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getCover(), getAudioFile(), getReleasedAt());
    }

    @Override
    public String toString() {
        return "Track{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", cover='" + cover + '\'' +
                ", audioFile='" + audioFile + '\'' +
                ", releasedAt=" + releasedAt +
                '}';
    }
}
