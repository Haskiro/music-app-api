package com.github.haskiro.musicapp.controllers;

import com.github.haskiro.musicapp.dto.artistDTO.ArtistDTO;
import com.github.haskiro.musicapp.dto.trackDTO.TrackDTO;
import com.github.haskiro.musicapp.models.Artist;
import com.github.haskiro.musicapp.models.Track;
import com.github.haskiro.musicapp.services.TrackService;
import com.github.haskiro.musicapp.util.ErrorResponse;
import com.github.haskiro.musicapp.util.exceptions.ArtistCreateUpdateException;
import com.github.haskiro.musicapp.util.exceptions.TrackCreateUpdateException;
import com.github.haskiro.musicapp.util.exceptions.TrackNotFoundException;
import com.github.haskiro.musicapp.util.exceptions.UserNotFoundException;
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
@RequestMapping("/api/tracks")
public class TrackController {
    private final TrackService trackService;
    private final ModelMapper modelMapper;

    @Autowired
    public TrackController(TrackService trackService, ModelMapper modelMapper) {
        this.trackService = trackService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public List<TrackDTO> findAll() {
        return trackService.findAll()
                .stream().map(this::converToTrackDTO).collect(Collectors.toList());
    }

    @PostMapping
    public ResponseEntity<HttpStatus> createTrack(@RequestBody @Valid TrackDTO trackDTO,
                                                  BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            String errorMessage = returnErrorsAsString(bindingResult);

            throw new TrackCreateUpdateException(errorMessage);
        }

        Track track = converToTrack(trackDTO);
        trackService.saveTrack(track);

        return ResponseEntity.ok(HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<HttpStatus> deleteTrack(@PathVariable("id") int id) {
        trackService.deleteTrack(id);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<HttpStatus> updateTrack(@PathVariable("id") int id,
                                                   @RequestBody @Valid TrackDTO request,
                                                   BindingResult bindingResult) {
        Track track = converToTrack(request);
        track.setId(id);

        if (bindingResult.hasErrors()) {
            String errorMessage = returnErrorsAsString(bindingResult);

            throw new TrackCreateUpdateException(errorMessage);
        }

        trackService.updateTrack(track);

        return ResponseEntity.ok(HttpStatus.OK);
    }

    private TrackDTO converToTrackDTO(Track track) {
        return modelMapper.map(track, TrackDTO.class);
    }

    private Track converToTrack(TrackDTO trackDTO) {
        return modelMapper.map(trackDTO, Track.class);
    }

    @ExceptionHandler
    private ResponseEntity<ErrorResponse> handleException(TrackCreateUpdateException e) {
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


}
