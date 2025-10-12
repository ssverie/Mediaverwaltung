package com.example.mediaverwaltung.service.importer;

import com.example.mediaverwaltung.model.MediaItem;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Arrays;
import java.util.List;

/**
 * JSON Importer für MediaItems
 */
@Component
public class JsonImporter implements DataImporter {
    
    @Override
    public List<MediaItem> importData(String filepath) throws Exception {
        
        InputStream inputStream = getClass().getClassLoader()
                .getResourceAsStream(filepath);
        
        if (inputStream == null) {
            throw new IllegalArgumentException("Datei nicht gefunden: " + filepath);
        }
        
        // Jackson ObjectMapper für JSON
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // Für LocalDate Support
        
        // JSON Array zu MediaItem List
        MediaItem[] itemsArray = mapper.readValue(inputStream, MediaItem[].class);
        
        return Arrays.asList(itemsArray);
    }
}