package com.example.mediaverwaltung;

import com.example.mediaverwaltung.model.MediaItem;
import com.example.mediaverwaltung.service.MediaItemService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import java.util.List;

@SpringBootApplication
public class MediaVerwaltungApplication {

    public static void main(String[] args) {
        SpringApplication.run(MediaVerwaltungApplication.class, args);
    }
    
    
    @Bean
    @Profile("!test")  // <- Diese Zeile hinzufügen!
    public CommandLineRunner demo(MediaItemService service) {
    	
    	
        return (args) -> {
            System.out.println("✅ MediaVerwaltung gestartet!");
            System.out.println("🌐 API: http://localhost:8080/api/media");
        };
    	
    	
//        return (args) -> {
//            
//            // 1) INSERT
//            System.out.println("\n📥 SCHRITT 1: INSERT (CSV-Import)\n");
//            try {
//                int imported = service.importFromCsv("data/media_items.csv");
//                System.out.println("✅ " + imported + " Items importiert!\n");
//            } catch (Exception e) {
//                System.err.println("❌ Import fehlgeschlagen: " + e.getMessage());
//            }
//            
//            // JSON IMPORT
//            System.out.println("\n📥 JSON-Import:\n");
//            try {
//                int jsonImported = service.importFromJson("data/media_items.json");
//                System.out.println("✅ " + jsonImported + " Items aus JSON importiert!\n");
//            } catch (Exception e) {
//                System.err.println("❌ JSON-Import fehlgeschlagen: " + e.getMessage());
//            }
//            
//            // 2) SELECT
//            System.out.println("\n📋 SCHRITT 2: SELECT (Alle Items)\n");
//            List<MediaItem> items = service.findAll();
//            System.out.println("Gefunden: " + items.size() + " Items\n");
//            for (MediaItem item : items) {
//                System.out.println("ID " + item.getId() + ": " + item.getBeschreibung());
//            }
//            
//            // 3) UPDATE
//            System.out.println("\n✏️ SCHRITT 3: UPDATE (Beschreibung → GROSS)\n");
//            for (MediaItem item : items) {
//                if (item.getBeschreibung() != null) {
//                    String alt = item.getBeschreibung();
//                    item.setBeschreibung(alt.toUpperCase());
//                    service.save(item);
//                    System.out.println("✅ Update ID " + item.getId() + ": " + alt + " → " + item.getBeschreibung());
//                }
//            }
//            
//            // SELECT nach UPDATE
//            System.out.println("\n📋 NACH UPDATE:\n");
//            items = service.findAll();
//            for (MediaItem item : items) {
//                System.out.println("ID " + item.getId() + ": " + item.getBeschreibung());
//            }
//            
//            // 4) DELETE - Letzte 5
//            System.out.println("\n🗑️ SCHRITT 4: DELETE (Letzte 5 Einträge)\n");
//            items = service.findAll();
//            int totalCount = items.size();
//            System.out.println("Aktuell in DB: " + totalCount + " Items");
//            System.out.println("Lösche die letzten 5...\n");
//            
//            for (int i = totalCount - 5; i < totalCount; i++) {
//                MediaItem itemToDelete = items.get(i);
//                System.out.println("🗑️ Lösche ID " + itemToDelete.getId() + ": " + itemToDelete.getBeschreibung());
//                //service.deleteById(itemToDelete.getId());
//            }
//            
//            // FINAL SELECT
//            System.out.println("\n📋 FINAL SELECT (Nach DELETE)\n");
//            items = service.findAll();
//            System.out.println("Verbleibend in DB: " + items.size() + " Item(s)\n");
//            for (MediaItem item : items) {
//                System.out.println("ID " + item.getId() + ": " + item.getBeschreibung());
//            }
//            
//            System.out.println("\n✅ FERTIG!\n");
//        };
    	
    	
    	
    }
    
    
    
}