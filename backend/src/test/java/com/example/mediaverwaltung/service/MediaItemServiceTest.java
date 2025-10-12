package com.example.mediaverwaltung.service;

import com.example.mediaverwaltung.model.MediaItem;
import com.example.mediaverwaltung.repository.MediaItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * Service Tests für MediaItemService
 * 
 * @ExtendWith(MockitoExtension.class) = Mockito für Mocks
 * Testet Service-Logik OHNE echte Datenbank
 */
@ExtendWith(MockitoExtension.class)
class MediaItemServiceTest {
    
    @Mock
    private MediaItemRepository repository;
    
    @InjectMocks
    private MediaItemService service;
    
    @Test
    void should_find_all_items() {
        // Given: Repository gibt 2 Items zurück
        MediaItem item1 = createTestItem(1L, "https://test1.com");
        MediaItem item2 = createTestItem(2L, "https://test2.com");
        when(repository.findAll()).thenReturn(Arrays.asList(item1, item2));
        
        // When: Service holt alle Items
        List<MediaItem> items = service.findAll();
        
        // Then: 2 Items zurück
        assertThat(items).hasSize(2);
        verify(repository, times(1)).findAll();
    }
    
    @Test
    void should_find_item_by_id() {
        // Given: Item mit ID 1 existiert
        MediaItem item = createTestItem(1L, "https://test.com");
        when(repository.findById(1L)).thenReturn(Optional.of(item));
        
        // When: Service sucht Item by ID
        MediaItem found = service.findById(1L);
        
        // Then: Item gefunden
        assertThat(found).isNotNull();
        assertThat(found.getId()).isEqualTo(1L);
        verify(repository, times(1)).findById(1L);
    }
    
    @Test
    void should_throw_exception_when_item_not_found() {
        // Given: Item mit ID 999 existiert NICHT
        when(repository.findById(999L)).thenReturn(Optional.empty());
        
        // When/Then: Exception wird geworfen
        assertThatThrownBy(() -> service.findById(999L))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("MediaItem nicht gefunden: 999");
    }
    
    @Test
    void should_save_item() {
        // Given: Ein neues Item
        MediaItem item = createTestItem(null, "https://test.com");
        MediaItem saved = createTestItem(1L, "https://test.com");
        when(repository.save(any(MediaItem.class))).thenReturn(saved);
        
        // When: Service speichert Item
        MediaItem result = service.save(item);
        
        // Then: Item wurde gespeichert mit ID
        assertThat(result.getId()).isEqualTo(1L);
        verify(repository, times(1)).save(item);
    }
    
    @Test
    void should_delete_item() {
        // Given: Repository deleteById macht nichts (void)
        doNothing().when(repository).deleteById(1L);
        
        // When: Service löscht Item
        service.deleteById(1L);
        
        // Then: deleteById wurde aufgerufen
        verify(repository, times(1)).deleteById(1L);
    }
    
    // Helper Method
    private MediaItem createTestItem(Long id, String url) {
        MediaItem item = new MediaItem();
        item.setId(id);
        item.setUrl(url);
        item.setBeschreibung("Test Item");
        item.setChannel("Test Channel");
        item.setMediaType("VIDEO");
        return item;
    }
}
