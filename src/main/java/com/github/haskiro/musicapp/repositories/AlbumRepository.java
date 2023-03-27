package com.github.haskiro.musicapp.repositories;

import com.github.haskiro.musicapp.models.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Integer> {

}
