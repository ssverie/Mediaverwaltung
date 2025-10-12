package com.example.mediaverwaltung;

import com.example.mediaverwaltung.model.MediaItem;
import com.example.mediaverwaltung.repository.MediaItemRepository;
import com.example.mediaverwaltung.service.MediaItemService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Integration Tests für MediaVerwaltung
 * 
 * @SpringBootTest = Vollständiger Spring Boot Context wird gestartet
 * webEnvironment = RANDOM_PORT = Server läuft auf zufälligem Port
 * 
 * Diese Klasse testet:
 * - Spring Boot startet korrekt
 * - Alle Beans sind konfiguriert
 * - REST API ist erreichbar
 * - Ende-zu-Ende Funktionalität
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // <- DIESE ZEILE HINZUFÜGEN!
class MediaVerwaltungApplicationTests {
    
    @LocalServerPort
    private int port;
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Autowired
    private MediaItemService service;
    
    @Autowired
    private MediaItemRepository repository;
    
    /**
     * Smoke Test: Prüft, ob Spring Boot Context startet
     */
    @Test
    void contextLoads() {
        // Wenn dieser Test durchläuft, ist Spring Boot korrekt gestartet
        assertThat(service).isNotNull();
        assertThat(repository).isNotNull();
    }
    
    /**
     * Prüft, ob alle wichtigen Beans vorhanden sind
     */
    @Test
    void should_have_all_required_beans() {
        assertThat(service).isNotNull();
        assertThat(repository).isNotNull();
    }
    
    /**
     * Integration Test: REST API erreichbar?
     */
    @Test
    void should_get_all_media_items_via_rest_api() {
        // When: GET /api/media
        String url = "http://localhost:" + port + "/api/media";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        
        // Then: HTTP 200 OK
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("["); // JSON Array
    }
    
    /**
     * Ende-zu-Ende Test: Create Item via REST API
     */
    @Test
    void should_create_media_item_via_rest_api() {
        // Given: Ein neues MediaItem
        MediaItem newItem = new MediaItem();
        newItem.setUrl("https://integration-test.com");
        newItem.setBeschreibung("Integration Test Item");
        newItem.setMediaType("VIDEO");
        
        // When: POST /api/media
        String url = "http://localhost:" + port + "/api/media";
        ResponseEntity<MediaItem> response = restTemplate.postForEntity(url, newItem, MediaItem.class);
        
        // Then: HTTP 201 Created
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isNotNull();
        assertThat(response.getBody().getUrl()).isEqualTo("https://integration-test.com");
        assertThat(response.getBody().getLastUpdatedAt()).isNotNull(); // @PrePersist!
    }
    
    /**
     * Ende-zu-Ende Test: Create, Read, Update, Delete
     */
    @Test
    void should_perform_full_crud_cycle() {
        String baseUrl = "http://localhost:" + port + "/api/media";
        
        // 1. CREATE
        MediaItem newItem = new MediaItem();
        newItem.setUrl("https://crud-test.com");
        newItem.setBeschreibung("CRUD Test");
        newItem.setMediaType("VIDEO");
        
        ResponseEntity<MediaItem> createResponse = restTemplate.postForEntity(baseUrl, newItem, MediaItem.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Long itemId = createResponse.getBody().getId();
        
        // 2. READ
        ResponseEntity<MediaItem> readResponse = restTemplate.getForEntity(baseUrl + "/" + itemId, MediaItem.class);
        assertThat(readResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readResponse.getBody().getUrl()).isEqualTo("https://crud-test.com");
        
        // 3. UPDATE
        MediaItem updateItem = readResponse.getBody();
        updateItem.setBeschreibung("Updated!");
        restTemplate.put(baseUrl + "/" + itemId, updateItem);
        
        ResponseEntity<MediaItem> updatedResponse = restTemplate.getForEntity(baseUrl + "/" + itemId, MediaItem.class);
        assertThat(updatedResponse.getBody().getBeschreibung()).isEqualTo("Updated!");
        
        // 4. DELETE
        restTemplate.delete(baseUrl + "/" + itemId);
        
        ResponseEntity<String> deletedResponse = restTemplate.getForEntity(baseUrl + "/" + itemId, String.class);
        assertThat(deletedResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    
    /**
     * Prüft, ob H2 Datenbank funktioniert
     */
    @Test
    void should_save_and_retrieve_from_database() {
        // Given: Ein neues Item
        MediaItem item = new MediaItem();
        item.setUrl("https://db-test.com");
        item.setBeschreibung("DB Test");
        item.setMediaType("AUDIO");
        
        // When: Über Service speichern
        MediaItem saved = service.save(item);
        
        // Then: Item in DB vorhanden
        assertThat(saved.getId()).isNotNull();
        
        MediaItem found = service.findById(saved.getId());
        assertThat(found.getUrl()).isEqualTo("https://db-test.com");
        assertThat(found.getLastUpdatedAt()).isNotNull();
        
        // Cleanup
        service.deleteById(saved.getId());
    }
    
    /**
     * Prüft, ob @PrePersist und @PreUpdate funktionieren
     */
    @Test
    void should_set_last_updated_at_automatically() throws InterruptedException {
        // Given: Ein neues Item
        MediaItem item = new MediaItem();
        item.setUrl("https://timestamp-test.com");
        item.setMediaType("TEXT");
        
        // When: Speichern
        MediaItem saved = service.save(item);
        
        // Then: lastUpdatedAt ist gesetzt (@PrePersist)
        assertThat(saved.getLastUpdatedAt()).isNotNull();
        
        // Wait 1 second
        Thread.sleep(1000);
        
        // When: Update
        saved.setBeschreibung("Updated!");
        MediaItem updated = service.save(saved);
        
        // Then: lastUpdatedAt wurde aktualisiert (@PreUpdate)
        assertThat(updated.getLastUpdatedAt()).isAfter(saved.getLastUpdatedAt());
        
        // Cleanup
        service.deleteById(updated.getId());
    }
}
