package com.github.haskiro.musicapp.controllers;

import com.github.haskiro.musicapp.dto.albumDTO.AlbumDTO;
import com.github.haskiro.musicapp.dto.albumDTO.AlbumWithTracksDTO;
import com.github.haskiro.musicapp.models.Album;
import com.github.haskiro.musicapp.services.AlbumService;
import com.github.haskiro.musicapp.util.ErrorResponse;
import com.github.haskiro.musicapp.util.exceptions.*;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.haskiro.musicapp.util.ErrorUtil.returnErrorsAsString;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private final AlbumService albumService;
    private final ModelMapper modelMapper;
    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public AlbumController(AlbumService albumService, ModelMapper modelMapper) {
        this.albumService = albumService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<AlbumDTO> findAll() {
        return albumService.findAll().stream().map(this::converToAlbumDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public AlbumWithTracksDTO findById(@PathVariable("id") int id) {
        return converToAlbumWithTracksDTO(albumService.findById(id));
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createAlbum(@RequestBody @Valid AlbumDTO albumDTO,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = returnErrorsAsString(bindingResult);

            throw new AlbumCreateUpdateException(errorMessage);
        }

        albumService.saveAlbum(convertToAlbum(albumDTO));

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{id}/upload-image")
    public ResponseEntity<HttpStatus> uploadPhoto(@PathVariable("id") int id,
                                                  @RequestPart MultipartFile image) {

        albumService.setCover(id, image);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{album_id}/tracks/{track_id}")
    public ResponseEntity<HttpStatus> setRelationBetweenAlbumAndTrack(
            @PathVariable("album_id") int albumId,
            @PathVariable("track_id") int trackId
    ) {
        albumService.setRelationBetweenAlbumAndTrack(albumId, trackId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping("/{album_id}/artists/{artist_id}")
    public ResponseEntity<HttpStatus> setRelationBetweenAlbumAndAlbum(
            @PathVariable("album_id") int albumId,
            @PathVariable("artist_id") int artistId
    ) {
        albumService.setRelationBetweenAlbumAndAlbum(albumId, artistId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private <T> Album convertToAlbum(T dto) {
        return modelMapper.map(dto, Album.class);
    }
    private AlbumDTO converToAlbumDTO(Album album) {
        return modelMapper.map(album, AlbumDTO.class);
    }

    private AlbumWithTracksDTO converToAlbumWithTracksDTO(Album album) {
        return modelMapper.map(album, AlbumWithTracksDTO.class);
    }


    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AlbumNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Album not found",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(AlbumCreateUpdateException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(MissingServletRequestPartException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ArtistNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Artist not found",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(TrackNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Track not found",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
