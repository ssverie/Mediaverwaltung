package com.example.mediaverwaltung.repository;

import com.example.mediaverwaltung.model.MediaItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Repository Tests für MediaItem
 * 
 * @DataJpaTest = Nur Datenbank-Layer wird getestet
 * @AutoConfigureTestDatabase = Nutzt unsere eigene DB-Config
 * Nutzt H2 In-Memory Database
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")  // <- DIESE ZEILE HINZUFÜGEN!
class MediaItemRepositoryTest {
    
    @Autowired
    private MediaItemRepository repository;
    
    @Test
    void should_save_and_find_media_item() {
        // Given: Ein neues MediaItem
        MediaItem item = new MediaItem();
        item.setUrl("https://test.com");
        item.setBeschreibung("Test Video");
        item.setChannel("Test Channel");
        item.setMediaType("VIDEO");
        
        // When: Item speichern
        MediaItem saved = repository.save(item);
        
        // Then: Item wurde gespeichert mit ID
        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getUrl()).isEqualTo("https://test.com");
        assertThat(saved.getLastUpdatedAt()).isNotNull(); // @PrePersist!
    }
    
    @Test
    void should_find_all_media_items() {
        // Given: 3 Items in DB
        repository.save(createTestItem("https://test1.com", "VIDEO"));
        repository.save(createTestItem("https://test2.com", "AUDIO"));
        repository.save(createTestItem("https://test3.com", "TEXT"));
        
        // When: Alle Items holen
        List<MediaItem> items = repository.findAll();
        
        // Then: 3 Items gefunden
        assertThat(items).hasSize(3);
    }
    
    @Test
    void should_find_item_by_id() {
        // Given: Ein gespeichertes Item
        MediaItem saved = repository.save(createTestItem("https://test.com", "VIDEO"));
        
        // When: Item by ID suchen
        Optional<MediaItem> found = repository.findById(saved.getId());
        
        // Then: Item gefunden
        assertThat(found).isPresent();
        assertThat(found.get().getUrl()).isEqualTo("https://test.com");
    }
    
    @Test
    void should_update_media_item() {
        // Given: Ein gespeichertes Item
        MediaItem item = repository.save(createTestItem("https://test.com", "VIDEO"));
        Long id = item.getId();
        
        // When: Item ändern und speichern
        item.setBeschreibung("Geändert!");
        repository.save(item);
        
        // Then: Änderung gespeichert
        MediaItem updated = repository.findById(id).orElseThrow();
        assertThat(updated.getBeschreibung()).isEqualTo("Geändert!");
    }
    
    @Test
    void should_delete_media_item() {
        // Given: Ein gespeichertes Item
        MediaItem item = repository.save(createTestItem("https://test.com", "VIDEO"));
        Long id = item.getId();
        
        // When: Item löschen
        repository.deleteById(id);
        
        // Then: Item ist weg
        Optional<MediaItem> found = repository.findById(id);
        assertThat(found).isEmpty();
    }
    
    // Helper Method
    private MediaItem createTestItem(String url, String mediaType) {
        MediaItem item = new MediaItem();
        item.setUrl(url);
        item.setBeschreibung("Test Item");
        item.setChannel("Test Channel");
        item.setMediaType(mediaType);
        return item;
    }
}
