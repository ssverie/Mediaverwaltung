package com.example.mediaverwaltung.controller;

import com.example.mediaverwaltung.model.MediaItem;
import com.example.mediaverwaltung.service.MediaItemService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Controller Tests für MediaItemController (REST API)
 * 
 * @WebMvcTest = Nur Controller-Layer wird getestet
 * MockMvc = Simuliert HTTP-Requests ohne Server zu starten
 */
@WebMvcTest(MediaItemController.class)
class MediaItemControllerTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private ObjectMapper objectMapper;
    
    @MockBean
    private MediaItemService service;
    
    @Test
    void should_get_all_media_items() throws Exception {
        // Given: Service gibt 2 Items zurück
        MediaItem item1 = createTestItem(1L, "https://test1.com");
        MediaItem item2 = createTestItem(2L, "https://test2.com");
        when(service.findAll()).thenReturn(Arrays.asList(item1, item2));
        
        // When/Then: GET /api/media
        mockMvc.perform(get("/api/media"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].url").value("https://test1.com"))
            .andExpect(jsonPath("$[1].id").value(2));
        
        verify(service, times(1)).findAll();
    }
    
    @Test
    void should_get_media_item_by_id() throws Exception {
        // Given: Item mit ID 1 existiert
        MediaItem item = createTestItem(1L, "https://test.com");
        when(service.findById(1L)).thenReturn(item);
        
        // When/Then: GET /api/media/1
        mockMvc.perform(get("/api/media/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.url").value("https://test.com"))
            .andExpect(jsonPath("$.beschreibung").value("Test Item"));
        
        verify(service, times(1)).findById(1L);
    }
    
    @Test
    void should_return_404_when_item_not_found() throws Exception {
        // Given: Item mit ID 999 existiert NICHT
        when(service.findById(999L)).thenThrow(new RuntimeException("Not found"));
        
        // When/Then: GET /api/media/999 → 404
        mockMvc.perform(get("/api/media/999"))
            .andExpect(status().isNotFound());
    }
    
    @Test
    void should_create_new_media_item() throws Exception {
        // Given: Ein neues Item wird erstellt
        MediaItem newItem = createTestItem(null, "https://new.com");
        MediaItem saved = createTestItem(1L, "https://new.com");
        when(service.save(any(MediaItem.class))).thenReturn(saved);
        
        // When/Then: POST /api/media
        mockMvc.perform(post("/api/media")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(newItem)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.url").value("https://new.com"));
        
        verify(service, times(1)).save(any(MediaItem.class));
    }
    
    @Test
    void should_update_media_item() throws Exception {
        // Given: Item mit ID 1 wird geupdatet
        MediaItem updated = createTestItem(1L, "https://updated.com");
        updated.setBeschreibung("Updated!");
        
        when(service.findById(1L)).thenReturn(updated);
        when(service.save(any(MediaItem.class))).thenReturn(updated);
        
        // When/Then: PUT /api/media/1
        mockMvc.perform(put("/api/media/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.beschreibung").value("Updated!"));
        
        verify(service, times(1)).findById(1L);
        verify(service, times(1)).save(any(MediaItem.class));
    }
    
    @Test
    void should_delete_media_item() throws Exception {
        // Given: Item mit ID 1 wird gelöscht
        doNothing().when(service).deleteById(1L);
        
        // When/Then: DELETE /api/media/1
        mockMvc.perform(delete("/api/media/1"))
            .andExpect(status().isNoContent());
        
        verify(service, times(1)).deleteById(1L);
    }
    
    @Test
    void should_get_item_count() throws Exception {
        // Given: Service gibt 5 Items zurück
        when(service.findAll()).thenReturn(Arrays.asList(
            createTestItem(1L, "url1"),
            createTestItem(2L, "url2"),
            createTestItem(3L, "url3"),
            createTestItem(4L, "url4"),
            createTestItem(5L, "url5")
        ));
        
        // When/Then: GET /api/media/count
        mockMvc.perform(get("/api/media/count"))
            .andExpect(status().isOk())
            .andExpect(content().string("5"));
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
