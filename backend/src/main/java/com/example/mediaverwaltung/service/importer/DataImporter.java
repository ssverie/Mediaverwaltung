package com.example.mediaverwaltung.service.importer;

import com.example.mediaverwaltung.model.MediaItem;
import java.util.List;

/**
 * Interface für verschiedene Daten-Import Formate
 * Ermöglicht einfaches Hinzufügen neuer Formate (CSV, JSON, XML)
 */
public interface DataImporter {
    
    /**
     * Importiert MediaItems aus einer Datei
     * 
     * @param filepath Pfad zur Import-Datei
     * @return Liste von importierten MediaItems
     * @throws Exception bei Fehlern beim Import
     */
    List<MediaItem> importData(String filepath) throws Exception;
}