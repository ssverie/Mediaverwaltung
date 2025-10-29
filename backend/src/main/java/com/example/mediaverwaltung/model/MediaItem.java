package com.example.mediaverwaltung.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * Entity für MediaItem
 * 
 * Speichert Medien-Links (Videos, Audio, Text)
 * 
 * @author Sven
 * @version 2.0 (2025-10-10)
 */
@Entity
@Table(name = "media_item")
@Data
public class MediaItem {
    
    /**
     * Primary Key (auto-increment)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    /**
     * Letzter Update-Zeitpunkt
     * Wird automatisch gesetzt bei INSERT und UPDATE
     */
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;
    
    /**
     * URL zur Medien-Quelle (required!)
     */
    @Column(nullable = false, length = 1000)
    private String url;
    
    /**
     * Beschreibung / Titel
     */
    @Column(length = 1000)
    private String beschreibung;
    
    /**
     * Channel / Creator Name
     */
    @Column(length = 255)
    private String channel;
    
    /**
     * Dauer (Format: HH:MM:SS oder MM:SS)
     */
    @Column(length = 255)
    private String dauer;
    
    
    @Column(name = "gesehen", nullable = false)
    private Boolean gesehen = false;
    
    /**
     * Stichwörter / Tags (komma-separiert)
     */
    @Column(length = 255)
    private String stichwort;
    
    /**
     * Media-Typ: VIDEO, AUDIO, TEXT
     */
    @Column(name = "media_type", length = 255)
    private String mediaType;
    
    /**
     * Lifecycle-Hook: Wird vor INSERT aufgerufen
     * Setzt lastUpdatedAt auf aktuelle Zeit
     */
    @PrePersist
    protected void onCreate() {
        lastUpdatedAt = LocalDateTime.now();
    }
    
    /**
     * Lifecycle-Hook: Wird vor UPDATE aufgerufen
     * Aktualisiert lastUpdatedAt auf aktuelle Zeit
     */
    @PreUpdate
    protected void onUpdate() {
        lastUpdatedAt = LocalDateTime.now();
    }
}
