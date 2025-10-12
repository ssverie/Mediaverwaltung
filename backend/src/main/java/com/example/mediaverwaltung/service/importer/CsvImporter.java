package com.example.mediaverwaltung.service.importer;

import com.example.mediaverwaltung.model.MediaItem;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * CSV Importer für MediaItems
 * Liest CSV-Dateien und konvertiert zu MediaItem-Objekten
 * 
 * CSV-Format (neue Version):
 * URL,BESCHREIBUNG,CHANNEL,DAUER,STICHWORT,MEDIA_TYPE
 * 
 * @author Sven
 * @version 2.0 (2025-10-10)
 */
@Component
public class CsvImporter implements DataImporter {
    
    @Override
    public List<MediaItem> importData(String filepath) throws Exception {
        List<MediaItem> items = new ArrayList<>();
        
        // CSV aus Resources laden
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(filepath);
        
        if (inputStream == null) {
            throw new IllegalArgumentException("Datei nicht gefunden: " + filepath);
        }
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            
            // Erste Zeile (Header) überspringen
            String headerLine = reader.readLine();
            if (headerLine == null) {
                throw new IllegalArgumentException("CSV-Datei ist leer");
            }
            
            // Datenzeilen verarbeiten
            String line;
            int lineNumber = 2; // Start bei 2 (Header = 1)
            
            while ((line = reader.readLine()) != null) {
                try {
                    MediaItem item = parseCsvLine(line);
                    items.add(item);
                } catch (Exception e) {
                    System.err.println("⚠️ Fehler in Zeile " + lineNumber + ": " + e.getMessage());
                    // Zeile überspringen, weitermachen
                }
                lineNumber++;
            }
        }
        
        return items;
    }
    
    /**
     * Parst eine CSV-Zeile zu einem MediaItem
     * 
     * Format: URL,BESCHREIBUNG,CHANNEL,DAUER,STICHWORT,MEDIA_TYPE
     * 
     * @param line CSV-Zeile
     * @return MediaItem-Objekt
     */
    private MediaItem parseCsvLine(String line) {
        // CSV-Splitting (einfache Version)
        // Für komplexere CSVs (mit Kommas in Daten): Apache Commons CSV nutzen
        String[] columns = line.split(",", -1); // -1 = keep empty strings
        
        MediaItem item = new MediaItem();
        
        // URL (required)
        if (columns.length > 0 && !columns[0].isEmpty()) {
            item.setUrl(columns[0].trim());
        } else {
            throw new IllegalArgumentException("URL ist Pflichtfeld!");
        }
        
        // BESCHREIBUNG
        if (columns.length > 1 && !columns[1].isEmpty()) {
            item.setBeschreibung(columns[1].trim());
        }
        
        // CHANNEL
        if (columns.length > 2 && !columns[2].isEmpty()) {
            item.setChannel(columns[2].trim());
        }
        
        // DAUER
        if (columns.length > 3 && !columns[3].isEmpty()) {
            item.setDauer(columns[3].trim());
        }
        
        // STICHWORT
        if (columns.length > 4 && !columns[4].isEmpty()) {
            item.setStichwort(columns[4].trim());
        }
        
        // MEDIA_TYPE
        if (columns.length > 5 && !columns[5].isEmpty()) {
            item.setMediaType(columns[5].trim());
        }
        
        return item;
    }
}
