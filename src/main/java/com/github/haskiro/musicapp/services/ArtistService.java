package com.github.haskiro.musicapp.services;

import com.github.haskiro.musicapp.models.Artist;
import com.github.haskiro.musicapp.models.Track;
import com.github.haskiro.musicapp.repositories.ArtistRepository;
import com.github.haskiro.musicapp.util.exceptions.ArtistNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final TrackService trackService;

    @Autowired
    public ArtistService(ArtistRepository artistRepository, TrackService trackService) {
        this.artistRepository = artistRepository;
        this.trackService = trackService;
    }

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public Artist findById(int id) {
        Optional<Artist> artist = artistRepository.findById(id);

        if (artist.isEmpty())
            throw new ArtistNotFoundException();

        Hibernate.initialize(artist.get().getTrackList());

        return artist.get();
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void saveArtist(Artist artist) {
        artist.setCreatedAt(OffsetDateTime.now());

        artistRepository.save(artist);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void updateArtist(Artist artist) {

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void setRelationBetweenArtistAndTrack(int trackId, int artistId) {

        Track track = trackService.findById(trackId);
        Artist artist = findById(artistId);

        track.getArtistList().add(artist);
        artist.getTrackList().add(track);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void removeRelationBetweenArtistAndTrack(int trackId, int artistId) {
        Track track = trackService.findById(trackId);
        Artist artist = findById(artistId);

        track.getArtistList().remove(artist);
        artist.getTrackList().remove(track);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void deleteArtist(int id) {
        Artist artist = findById(id);
        artist.getTrackList().forEach(track -> {
            track.getArtistList().remove(artist);
        });

        artistRepository.deleteById(id);
    }
}
