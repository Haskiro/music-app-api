package com.github.haskiro.musicapp.controllers;

import com.github.haskiro.musicapp.dto.artistDTO.ArtistDTO;
import com.github.haskiro.musicapp.dto.artistDTO.ArtistWithTracksDTO;
import com.github.haskiro.musicapp.models.Artist;
import com.github.haskiro.musicapp.services.ArtistService;
import com.github.haskiro.musicapp.util.ArtistNotFoundException;
import com.github.haskiro.musicapp.util.ErrorResponse;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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

    private ArtistDTO converToArtistDTO(Artist artist) {
        return modelMapper.map(artist, ArtistDTO.class);
    }

    private ArtistWithTracksDTO converToArtistWithTracksDTO(Artist artist) {
        return modelMapper.map(artist, ArtistWithTracksDTO.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(ArtistNotFoundException e) {
        ErrorResponse response = new ErrorResponse(
                "Artist with this id not found",
                LocalDateTime.now()
        );

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
