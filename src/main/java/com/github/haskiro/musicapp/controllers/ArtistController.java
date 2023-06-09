package com.github.haskiro.musicapp.controllers;

import com.github.haskiro.musicapp.dto.artistDTO.ArtistDTO;
import com.github.haskiro.musicapp.dto.artistDTO.ArtistWithTracksAndAlbumsDTO;
import com.github.haskiro.musicapp.dto.artistDTO.CreateArtistDTO;
import com.github.haskiro.musicapp.models.Artist;
import com.github.haskiro.musicapp.services.ArtistService;
import com.github.haskiro.musicapp.services.TrackService;
import com.github.haskiro.musicapp.util.exceptions.ArtistCreateUpdateException;
import com.github.haskiro.musicapp.util.exceptions.ArtistNotFoundException;
import com.github.haskiro.musicapp.util.ErrorResponse;
import com.github.haskiro.musicapp.util.exceptions.FileUploadException;
import com.github.haskiro.musicapp.util.exceptions.TrackNotFoundException;
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
@RequestMapping("/api/artists")
public class ArtistController {
    private final ArtistService artistService;
    private final ModelMapper modelMapper;
    private final TrackService trackService;

    @Value("${upload.path}")
    private String uploadPath;

    @Autowired
    public ArtistController(ArtistService artistService, ModelMapper modelMapper, TrackService trackService) {
        this.artistService = artistService;
        this.modelMapper = modelMapper;
        this.trackService = trackService;
    }

    @GetMapping
    public List<ArtistDTO> findAll() {
        List<Artist> artists =  artistService.findAll();

        return artists.stream().map(this::converToArtistDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ArtistWithTracksAndAlbumsDTO findOneById(@PathVariable("id") int id) {
        Artist artist = artistService.findById(id);

        return convertToArtistWithTracksDTO(artist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteArtist(@PathVariable("id") int id) {
        artistService.deleteArtist(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }


    @PostMapping("/{artist_id}/tracks/{track_id}")
    public ResponseEntity<HttpStatus> setRelationBetweenArtistAndTrack(
            @PathVariable("artist_id") int artistId,
            @PathVariable("track_id") int trackId
    ) {
        artistService.setRelationBetweenArtistAndTrack(trackId, artistId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/{artist_id}/tracks/{track_id}")
    public ResponseEntity<HttpStatus> removeRelationBetweenArtistAndTrack(
            @PathVariable("artist_id") int artistId,
            @PathVariable("track_id") int trackId
    ) {
        artistService.removeRelationBetweenArtistAndTrack(trackId, artistId);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createArtist(@RequestBody @Valid CreateArtistDTO request,
                                                   BindingResult bindingResult) {

        Artist artist = convertToArtist(request);

        if (bindingResult.hasErrors()) {
            String errorMessage = returnErrorsAsString(bindingResult);

            throw new ArtistCreateUpdateException(errorMessage);
        }

        artistService.saveArtist(artist);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @PostMapping("/{id}/upload-photo")
    public ResponseEntity<HttpStatus> uploadPhoto(@PathVariable("id") int id,
            @RequestPart MultipartFile photo) {

        artistService.setPhoto(id, photo);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateArtist(@PathVariable("id") int id,
                                                   @RequestBody @Valid ArtistDTO request,
                                                   BindingResult bindingResult) {
        Artist artist = convertToArtist(request);
        artist.setId(id);

        if (bindingResult.hasErrors()) {
            String errorMessage = returnErrorsAsString(bindingResult);

            throw new ArtistCreateUpdateException(errorMessage);
        }

        artistService.updateArtist(artist);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private ArtistDTO converToArtistDTO(Artist artist) {
        return modelMapper.map(artist, ArtistDTO.class);
    }

    private <T> Artist convertToArtist(T dto) {
        return modelMapper.map(dto, Artist.class);
    }

    private ArtistWithTracksAndAlbumsDTO convertToArtistWithTracksDTO(Artist artist) {
        return modelMapper.map(artist, ArtistWithTracksAndAlbumsDTO.class);
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
    private ResponseEntity<ErrorResponse> handleException(ArtistCreateUpdateException e) {
        ErrorResponse response = new ErrorResponse(
                e.getMessage(),
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(TrackNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Track not found",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(FileUploadException e) {
        ErrorResponse response = new ErrorResponse(
                "File uploading error",
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
}
