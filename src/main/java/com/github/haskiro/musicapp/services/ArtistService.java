package com.github.haskiro.musicapp.services;

import com.github.haskiro.musicapp.models.Artist;
import com.github.haskiro.musicapp.repositories.ArtistRepository;
import com.github.haskiro.musicapp.util.ArtistNotFoundException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class ArtistService {
    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    public Artist findOneById(int id) {
        Optional<Artist> artist = artistRepository.findById(id);

        if (artist.isEmpty())
            throw new ArtistNotFoundException();

        Hibernate.initialize(artist.get().getTrackList());

        return artist.get();
    }


}
