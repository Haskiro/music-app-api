package com.github.haskiro.musicapp.services;

import com.github.haskiro.musicapp.models.Album;
import com.github.haskiro.musicapp.models.Artist;
import com.github.haskiro.musicapp.models.Track;
import com.github.haskiro.musicapp.repositories.AlbumRepository;
import com.github.haskiro.musicapp.util.exceptions.AlbumNotFoundException;
import com.github.haskiro.musicapp.util.exceptions.FileUploadException;
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
public class AlbumService {
    @Value("${upload.path}")
    private String uploadPath;
    
    private final AlbumRepository albumRepository;
    private final TrackService trackService;
    private final ArtistService artistService;

    @Autowired
    public AlbumService(AlbumRepository albumRepository, TrackService trackService, ArtistService artistService) {
        this.albumRepository = albumRepository;
        this.trackService = trackService;
        this.artistService = artistService;
    }
    
    public List<Album> findAll() {
        return albumRepository.findAll();
    }
    
    public Album findById(int id) {
        return albumRepository.findById(id).orElseThrow(AlbumNotFoundException::new);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void saveAlbum(Album album) {
        album.setCreatedAt(OffsetDateTime.now());
        albumRepository.save(album);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Transactional
    public void setCover(int id, MultipartFile image) {
        String fileUri =  uploadPath + "/albums/covers/" + UUID.randomUUID() + image.getOriginalFilename();

        saveFile(fileUri, Paths.get(uploadPath + "/albums/covers/"), image);

        Album album = findById(id);
        album.setCover(fileUri);
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

    public void setRelationBetweenAlbumAndTrack(int albumId, int trackId) {
        Track track = trackService.findById(trackId);
        Album album = findById(albumId);

        track.getAlbumList().add(album);
        album.getTrackList().add(track);
    }

    public void setRelationBetweenAlbumAndAlbum(int albumId, int artistId) {
        Artist artist = artistService.findById(artistId);
        Album album = findById(albumId);

        artist.getAlbumList().add(album);
        album.getArtistList().add(artist);
    }
}
