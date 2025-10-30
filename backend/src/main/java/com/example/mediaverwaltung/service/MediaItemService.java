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
    
    
 // ========================================
 // CSV EXPORT/IMPORT (NEU!)
 // ========================================

 /**
  * Exportiert alle MediaItems als CSV-String
  * Format: url,beschreibung,channel,dauer,gesehen,mediaType,stichwort
  * 
  * @return CSV-String mit allen Items
  */
 public String exportAllToCSV() {
     List<MediaItem> items = repository.findAll();
     
     StringBuilder csv = new StringBuilder();
     
     // Header
     csv.append("url,beschreibung,channel,dauer,gesehen,mediaType,stichwort\n");
     
     // Daten
     for (MediaItem item : items) {
         csv.append(escapeCsv(item.getUrl())).append(",");
         csv.append(escapeCsv(item.getBeschreibung())).append(",");
         csv.append(escapeCsv(item.getChannel())).append(",");
         csv.append(escapeCsv(item.getDauer())).append(",");
         csv.append(item.getGesehen() != null ? item.getGesehen() : false).append(",");
         csv.append(escapeCsv(item.getMediaType())).append(",");
         csv.append(escapeCsv(item.getStichwort())).append("\n");
     }
     
     return csv.toString();
 }

 /**
  * Importiert MediaItems aus CSV-String
  * STRATEGIE: REPLACE (Tabelle leeren + neu einfügen)
  * 
  * @param csvContent CSV-String (mit Header!)
  * @return Anzahl importierter Items
  */
 public int importFromCSVReplace(String csvContent) throws Exception {
     System.out.println("📥 Starte CSV-Upload (REPLACE-Strategie)");
     
     // 1. Tabelle leeren
     repository.deleteAll();
     System.out.println("🗑️  Alle bestehenden Items gelöscht");
     
     // 2. CSV parsen
     String[] lines = csvContent.split("\n");
     int count = 0;
     
     // Header überspringen (erste Zeile)
     for (int i = 1; i < lines.length; i++) {
         String line = lines[i].trim();
         if (line.isEmpty()) continue;
         
         try {
             String[] fields = parseCsvLine(line);
             
             if (fields.length >= 7) {
                 MediaItem item = new MediaItem();
                 item.setUrl(fields[0]);
                 item.setBeschreibung(fields[1]);
                 item.setChannel(fields[2]);
                 item.setDauer(fields[3]);
                 item.setGesehen(Boolean.parseBoolean(fields[4]));
                 item.setMediaType(fields[5]);
                 item.setStichwort(fields[6]);
                 
                 repository.save(item);
                 count++;
                 System.out.println("  ✓ Importiert: " + item.getBeschreibung());
             }
         } catch (Exception e) {
             System.err.println("  ✗ Fehler in Zeile " + (i+1) + ": " + e.getMessage());
         }
     }
     
     System.out.println("✅ Import abgeschlossen: " + count + " Items");
     return count;
 }

 /**
  * Escapet CSV-Werte (Kommas, Quotes)
  */
 private String escapeCsv(String value) {
     if (value == null) return "";
     
     // Wenn Komma oder Quote enthalten: In Quotes wrappen und Quotes verdoppeln
     if (value.contains(",") || value.contains("\"") || value.contains("\n")) {
         return "\"" + value.replace("\"", "\"\"") + "\"";
     }
     return value;
 }
 

 /**
  * Parst eine CSV-Zeile (berücksichtigt Quotes)
  */
 private String[] parseCsvLine(String line) {
     return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
 }
    
    
    
    
    
}