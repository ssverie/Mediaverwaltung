
package com.example.mediaverwaltung.controller;

import com.example.mediaverwaltung.model.MediaItem;
import com.example.mediaverwaltung.service.MediaItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * REST Controller für MediaItem CRUD Operations
 * 
 * Endpoints:
 * GET    /api/media          → Alle Items
 * GET    /api/media/{id}     → Einzelnes Item
 * POST   /api/media          → Neues Item erstellen
 * PUT    /api/media/{id}     → Item updaten
 * DELETE /api/media/{id}     → Item löschen
 */
@RestController
@RequestMapping("/api/media")
@CrossOrigin(origins = "*") // Später für React Frontend
public class MediaItemController {
    
    @Autowired
    private MediaItemService service;
    
    /**
     * GET /api/media
     * Gibt alle MediaItems zurück
     */
    @GetMapping
    public ResponseEntity<List<MediaItem>> getAllItems() {
        List<MediaItem> items = service.findAll();
        return ResponseEntity.ok(items);
    }
    
    /**
     * GET /api/media/{id}
     * Gibt einzelnes MediaItem zurück
     */
    @GetMapping("/{id}")
    public ResponseEntity<MediaItem> getItemById(@PathVariable Long id) {
        try {
            MediaItem item = service.findById(id);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * POST /api/media
     * Erstellt neues MediaItem
     * 
     * Body: JSON mit MediaItem-Daten (ohne id)
     */
    @PostMapping
    public ResponseEntity<MediaItem> createItem(@RequestBody MediaItem item) {
        try {
            MediaItem saved = service.save(item);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    /**
     * PUT /api/media/{id}
     * Updated existierendes MediaItem
     * 
     * Body: JSON mit geänderten MediaItem-Daten
     */
    @PutMapping("/{id}")
    public ResponseEntity<MediaItem> updateItem(
            @PathVariable Long id, 
            @RequestBody MediaItem updatedItem) {
        try {
            // Prüfen ob Item existiert
            service.findById(id);
            
            // ID setzen (wichtig für Update!)
            updatedItem.setId(id);
            
            MediaItem saved = service.save(updatedItem);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * DELETE /api/media/{id}
     * Löscht MediaItem
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try {
            service.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    /**
     * GET /api/media/count
     * Anzahl aller Items
     */
    @GetMapping("/count")
    public ResponseEntity<Long> getCount() {
        long count = service.findAll().size();
        return ResponseEntity.ok(count);
    }
    
    
    
    
 // ========================================
 // CSV DOWNLOAD/UPLOAD (NEU!)
 // ========================================

 /**
  * GET /api/media/download
  * Exportiert alle MediaItems als CSV-Datei
  */
 @GetMapping("/download")
 public ResponseEntity<String> downloadCSV() {
     try {
         String csv = service.exportAllToCSV();
         
         return ResponseEntity.ok()
                 .header("Content-Type", "text/csv; charset=UTF-8")
                 .header("Content-Disposition", "attachment; filename=mediaitems_" + 
                         java.time.LocalDate.now() + ".csv")
                 .body(csv);
     } catch (Exception e) {
         return ResponseEntity.internalServerError().build();
     }
 }

 /**
  * POST /api/media/upload
  * Importiert MediaItems aus CSV (REPLACE-Strategie)
  * 
  * Body: Raw CSV-Content als String
  */
 @PostMapping("/upload")
 public ResponseEntity<String> uploadCSV(@RequestBody String csvContent) {
     try {
         int count = service.importFromCSVReplace(csvContent);
         return ResponseEntity.ok("✅ Import erfolgreich: " + count + " Items importiert");
     } catch (Exception e) {
         return ResponseEntity.badRequest()
                 .body("❌ Import fehlgeschlagen: " + e.getMessage());
     }
 }
    
    
}