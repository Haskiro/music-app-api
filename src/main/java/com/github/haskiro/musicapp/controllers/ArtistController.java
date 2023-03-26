package com.github.haskiro.musicapp.controllers;

import com.github.haskiro.musicapp.dto.artistDTO.ArtistDTO;
import com.github.haskiro.musicapp.dto.artistDTO.ArtistWithTracksDTO;
import com.github.haskiro.musicapp.models.Artist;
import com.github.haskiro.musicapp.services.ArtistService;
import com.github.haskiro.musicapp.util.exceptions.ArtistCreateUpdateException;
import com.github.haskiro.musicapp.util.exceptions.ArtistNotFoundException;
import com.github.haskiro.musicapp.util.ErrorResponse;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.github.haskiro.musicapp.util.ErrorUtil.returnErrorsAsString;

@RestController
@RequestMapping("/api/artists")
public class ArtistController {
    private final ArtistService artistService;
    private final ModelMapper modelMapper;

    @Autowired
    public ArtistController(ArtistService artistService, ModelMapper modelMapper) {
        this.artistService = artistService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<ArtistDTO> findAll() {
        List<Artist> artists =  artistService.findAll();

        return artists.stream().map(this::converToArtistDTO).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ArtistWithTracksDTO findOneById(@PathVariable("id") int id) {
        Artist artist = artistService.findOneById(id);

        return converToArtistWithTracksDTO(artist);
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createArtist(@RequestBody @Valid ArtistDTO artistDTO,
                                       BindingResult bindingResult) {
        Artist artist = convertToArtist(artistDTO);

        if (bindingResult.hasErrors()) {
            String errorMessage = returnErrorsAsString(bindingResult);

            throw new ArtistCreateUpdateException(errorMessage);
        }

        artistService.saveArtist(artist);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    private ArtistDTO converToArtistDTO(Artist artist) {
        return modelMapper.map(artist, ArtistDTO.class);
    }

    private Artist convertToArtist(ArtistDTO artistDTO) {
        return modelMapper.map(artistDTO, Artist.class);
    }

    private ArtistWithTracksDTO converToArtistWithTracksDTO(Artist artist) {
        return modelMapper.map(artist, ArtistWithTracksDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ArtistNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Artist not Found",
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
}
