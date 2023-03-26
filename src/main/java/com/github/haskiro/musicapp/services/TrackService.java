package com.github.haskiro.musicapp.services;

import com.github.haskiro.musicapp.models.Artist;
import com.github.haskiro.musicapp.models.Track;
import com.github.haskiro.musicapp.repositories.TrackRepository;
import com.github.haskiro.musicapp.util.exceptions.TrackNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class TrackService {
    private final TrackRepository trackRepository;

    @Autowired
    public TrackService(TrackRepository trackRepository) {
        this.trackRepository = trackRepository;
    }

    public List<Track> findAll() {
        return trackRepository.findAll();
    }

    public Track findById(int id) {
        return trackRepository.findById(id)
                .orElseThrow(() -> new TrackNotFoundException());
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void saveTrack(Track track) {
        track.setCreatedAt(OffsetDateTime.now());

        trackRepository.save(track);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void deleteTrack(int id) {
        Track track = findById(id);
        track.getArtistList().forEach(artist -> {
            artist.getTrackList().remove(track);
        });

        trackRepository.deleteById(id);
    }
}
