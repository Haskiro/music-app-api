package com.github.haskiro.musicapp.services;

import com.github.haskiro.musicapp.models.Artist;
import com.github.haskiro.musicapp.models.Track;
import com.github.haskiro.musicapp.repositories.TrackRepository;
import com.github.haskiro.musicapp.util.exceptions.FileUploadException;
import com.github.haskiro.musicapp.util.exceptions.TrackNotFoundException;
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
import java.util.UUID;

@Service
@Transactional(readOnly = true)
public class TrackService {
    private final TrackRepository trackRepository;
    @Value("${upload.path}")
    private String uploadPath;

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

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void updateTrack(Track trackToBeUpdated) {
        Track track = findById(trackToBeUpdated.getId());

        trackToBeUpdated.setCreatedAt(track.getCreatedAt());
        trackToBeUpdated.setArtistList(track.getArtistList());

        trackRepository.save(trackToBeUpdated);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void setCover(int id, MultipartFile image) {
        String fileUri =  uploadPath + "/tracks/covers/" + UUID.randomUUID() + image.getOriginalFilename();

        saveFile(fileUri, Paths.get(uploadPath + "/tracks/covers/"), image);

        Track track = findById(id);
        track.setCover(fileUri);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void setAudio(int id, MultipartFile audio) {
        String fileUri =  uploadPath + "/tracks/audio/" + UUID.randomUUID() + audio.getOriginalFilename();

        saveFile(fileUri, Paths.get(uploadPath + "/tracks/audio/"), audio);

        Track track = findById(id);
        track.setAudioFile(fileUri);
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
