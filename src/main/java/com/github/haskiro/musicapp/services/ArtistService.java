package com.github.haskiro.musicapp.services;

import com.github.haskiro.musicapp.models.Artist;
import com.github.haskiro.musicapp.models.Track;
import com.github.haskiro.musicapp.repositories.ArtistRepository;
import com.github.haskiro.musicapp.util.exceptions.ArtistNotFoundException;
import com.github.haskiro.musicapp.util.exceptions.FileUploadException;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class ArtistService {
    private final ArtistRepository artistRepository;
    private final TrackService trackService;
    @Value("${upload.path}")
    private String uploadPath;

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
    public void updateArtist(Artist artistToBeUpdated) {
        Artist artist = findById(artistToBeUpdated.getId());

        artistToBeUpdated.setCreatedAt(artist.getCreatedAt());
        artistToBeUpdated.setTrackList(artist.getTrackList());

        artistRepository.save(artistToBeUpdated);
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

    @Transactional
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void setPhoto(int id, MultipartFile file) {
        String fileUri =  uploadPath + "/artists/" + UUID.randomUUID() + file.getOriginalFilename();

        saveFile(fileUri, Paths.get(uploadPath + "/artists/"), file);

        Artist artist = findById(id);
        artist.setPhoto(fileUri);
    }

    private void saveFile(String fileUri, Path fileUrl, MultipartFile multipartFile)  {
        try {
            Files.createDirectories(fileUrl);
            File file = new File(fileUri);
            multipartFile.transferTo(file);
        } catch (IOException e) {
            throw new FileUploadException();
        }
    }
}
