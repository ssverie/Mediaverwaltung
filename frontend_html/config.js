/**
 * MediaVerwaltung - Zentrale Konfiguration
 * 
 * Diese Datei wird von allen HTML-Seiten eingebunden.
 * Automatische Erkennung: Lokal vs. Production
 */

// Automatische API-URL Erkennung
const isLocalhost = window.location.hostname === 'localhost' 
                 || window.location.hostname === '127.0.0.1'
                 || window.location.hostname === ''
                 || window.location.protocol === 'file:';

// API Base URL
const API_BASE_URL = isLocalhost 
    ? 'http://localhost:8080'
    : 'https://mediaverwaltung.onrender.com';

// API Endpoints
const API = {
    // Base URL
    BASE: API_BASE_URL,
    
    // Media Endpoints
    MEDIA: `${API_BASE_URL}/api/media`,
    MEDIA_COUNT: `${API_BASE_URL}/api/media/count`,
    
    // Weitere Endpoints hier hinzufügen...
    // IMPORT: `${API_BASE_URL}/api/import`,
    // EXPORT: `${API_BASE_URL}/api/export`,
};

// Debug-Info in Console
console.log('🔧 Environment:', isLocalhost ? 'LOCAL' : 'PRODUCTION');
console.log('🔗 API Base URL:', API_BASE_URL);

// Für alte Kompatibilität (optional)
const API_URL = API.MEDIA;

// Config als globales Objekt verfügbar machen
window.APP_CONFIG = {
    API,
    isLocalhost,
    version: '1.0.0'
};