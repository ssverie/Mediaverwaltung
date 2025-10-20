package com.example.mediaverwaltung.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 * CORS Configuration für Frontend-Zugriff
 * Erlaubt Anfragen von GitHub Pages
 */
@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        
        // Erlaubt Credentials (Cookies, Authorization headers)
        config.setAllowCredentials(true);
        
        // Erlaubte Origins (Frontend URLs)
        config.addAllowedOriginPattern("*"); // Für Development - alle erlauben
        // Für Production später spezifischer:
        // config.addAllowedOrigin("https://ssverie.github.io");
        // config.addAllowedOrigin("http://localhost:5500"); // Live Server lokal
        
        // Erlaubte HTTP Methods
        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");
        config.addAllowedMethod("OPTIONS");
        
        // Erlaubte Headers
        config.addAllowedHeader("*");
        
        // Exposed Headers (für JavaScript sichtbar)
        config.addExposedHeader("Authorization");
        config.addExposedHeader("Content-Type");
        
        // Auf alle Endpoints anwenden
        source.registerCorsConfiguration("/**", config);
        
        return new CorsFilter(source);
    }
}