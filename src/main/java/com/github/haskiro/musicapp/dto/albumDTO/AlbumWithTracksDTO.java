package com.github.haskiro.musicapp.dto.albumDTO;

import com.github.haskiro.musicapp.dto.trackDTO.TrackDTO;
import com.github.haskiro.musicapp.models.Track;

import java.util.Set;

public class AlbumWithTracksDTO {
    private int id;
    private String title;
    private String description;
    private String cover;
    private Set<TrackDTO> tracks;

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

    public Set<TrackDTO> getTracks() {
        return tracks;
    }

    public void setTracks(Set<TrackDTO> tracks) {
        this.tracks = tracks;
    }
}
