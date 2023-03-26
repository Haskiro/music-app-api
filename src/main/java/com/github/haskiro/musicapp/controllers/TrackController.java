package com.github.haskiro.musicapp.controllers;

import com.github.haskiro.musicapp.dto.trackDTO.TrackDTO;
import com.github.haskiro.musicapp.models.Track;
import com.github.haskiro.musicapp.services.TrackService;
import com.github.haskiro.musicapp.util.ErrorResponse;
import com.github.haskiro.musicapp.util.exceptions.TrackCreateUpdateException;
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


}
