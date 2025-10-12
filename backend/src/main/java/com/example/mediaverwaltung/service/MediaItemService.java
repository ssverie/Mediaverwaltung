package com.example.mediaverwaltung.service;

import com.example.mediaverwaltung.model.MediaItem;
import com.example.mediaverwaltung.repository.MediaItemRepository;
import com.example.mediaverwaltung.service.importer.DataImporter;
import com.example.mediaverwaltung.service.importer.JsonImporter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service für MediaItem Business-Logik
 * Trennt Controller von Repository (Clean Architecture)
 */
@Service
@Transactional
public class MediaItemService {
    
    @Autowired
    private MediaItemRepository repository;
    
    @Autowired
    private DataImporter csvImporter; // Spring injiziert CsvImporter automatisch!
    
    @Autowired
    private JsonImporter jsonImporter; // Neben csvImporter!
    
    /**
     * Gibt alle MediaItems zurück
     */
    public List<MediaItem> findAll() {
        return repository.findAll();
    }
    
    /**
     * Findet MediaItem by ID
     */
    public MediaItem findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("MediaItem nicht gefunden: " + id));
    }
    
    /**
     * Speichert einzelnes MediaItem (CREATE oder UPDATE)
     */
    public MediaItem save(MediaItem item) {
        return repository.save(item);
    }
    
    /**
     * Löscht MediaItem by ID
     */
    public void deleteById(Long id) {
        repository.deleteById(id);
    }
    
    /**
     * Importiert MediaItems aus CSV-Datei
     * 
     * @param filepath Pfad zur CSV (relativ zu resources/)
     * @return Anzahl importierter Items
     */
    public int importFromCsv(String filepath) throws Exception {
        System.out.println("📥 Starte CSV-Import: " + filepath);
        
        // 1. CSV einlesen
        List<MediaItem> items = csvImporter.importData(filepath);
        
        System.out.println("✅ " + items.size() + " Items aus CSV gelesen");
        
        // 2. In Datenbank speichern
        int count = 0;
        for (MediaItem item : items) {
            try {
                repository.save(item);
                count++;
                System.out.println("  ✓ Gespeichert: " + item.getBeschreibung());
            } catch (Exception e) {
                System.err.println("  ✗ Fehler beim Speichern: " + item.getBeschreibung());
                System.err.println("    Grund: " + e.getMessage());
            }
        }
        
        System.out.println("✅ Import abgeschlossen: " + count + "/" + items.size() + " erfolgreich");
        
        return count;
    }
    
    /**
     * Importiert MediaItems aus JSON-Datei
     */
    public int importFromJson(String filepath) throws Exception {
        System.out.println("📥 Starte JSON-Import: " + filepath);
        
        // 1. JSON einlesen
        List<MediaItem> items = jsonImporter.importData(filepath);
        
        System.out.println("✅ " + items.size() + " Items aus JSON gelesen");
        
        // 2. In Datenbank speichern
        int count = 0;
        for (MediaItem item : items) {
            try {
                repository.save(item);
                count++;
                System.out.println("  ✓ Gespeichert: " + item.getBeschreibung());
            } catch (Exception e) {
                System.err.println("  ✗ Fehler: " + item.getBeschreibung());
            }
        }
        
        System.out.println("✅ JSON-Import abgeschlossen: " + count + "/" + items.size());
        
        return count;
    }
    
    
    
    
    
}