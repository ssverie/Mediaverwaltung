package com.example.mediaverwaltung.repository;

import com.example.mediaverwaltung.model.MediaItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaItemRepository extends JpaRepository<MediaItem, Long> {
    // Spring generiert automatisch alle CRUD-Methoden!
    // findAll(), findById(), save(), delete() etc.
}