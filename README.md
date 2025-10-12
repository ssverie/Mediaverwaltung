# 🎬 MediaVerwaltung

> Zentrale Verwaltung für Video-Links, Filme, Serien, Streams und mehr

Nie wieder vergessen, was du sehen wolltest! MediaVerwaltung hilft dir, alle deine Video-Links, Serien, Streams und Podcasts an einem Ort zu organisieren.

## ✨ Features

- ✅ **CRUD-Operationen** - Erstellen, Anzeigen, Bearbeiten, Löschen von Media-Items
- ✅ **REST API** - Vollständige Backend-API für alle Operationen
- ✅ **CSV/JSON Import** - Bulk-Import von bestehenden Daten
- ✅ **HTML Frontend** - Modernes Web-Interface mit Inline-Editing
- ✅ **Statistiken** - Übersicht über Views, Channels, etc.
- ✅ **Filter & Suche** - Schnelles Finden von Inhalten

## 🛠️ Tech Stack

**Backend:**
- Java 21
- Spring Boot 3.2
- Spring Data JPA
- H2 Database (TCP Mode)
- Lombok
- Jackson (JSON)

**Frontend:**
- HTML5 / CSS3
- Vanilla JavaScript
- Fetch API

**Tools:**
- Maven
- Eclipse IDE
- Git

## 📁 Projektstruktur

```
Mediaverwaltung/
├── backend/           # Spring Boot Backend
│   ├── src/main/java/
│   │   └── com/example/mediaverwaltung/
│   │       ├── model/           # Entity (MediaItem)
│   │       ├── repository/      # JPA Repository
│   │       ├── service/         # Business Logic + CSV/JSON Import
│   │       └── controller/      # REST API
│   └── pom.xml
├── database/          # SQL-Scripts
├── docs/              # Dokumentation
└── frontend_html/     # HTML/CSS/JS Frontend
    ├── index.html     # Homepage mit Statistiken
    ├── create.html    # Neues Item erstellen
    └── list.html      # Alle Items anzeigen/bearbeiten
```

## 🚀 Quick Start

### Voraussetzungen

- Java 21
- Maven 3.x
- H2 Database Server

### Installation

1. **Repository klonen:**
```bash
git clone https://github.com/ssverie/Mediaverwaltung.git
cd Mediaverwaltung
```

2. **H2 Datenbank starten:**
```bash
cd C:\Apps\h2\bin
h2-server.bat
java -cp h2-2.4.240.jar;%H2DRIVERS%;%CLASSPATH%" org.h2.tools.Server -tcp -web -ifNotExists -tcpAllowOthers

3. **Backend starten:**
```bash
cd backend
mvn spring-boot:run
```

4. **Frontend öffnen:**
```
Öffne im Browser: frontend_html/index.html
```

### Wichtige URLs

| Service | URL |
|---------|-----|
| Backend API | http://localhost:8080/api/media  |
| H2 Console  | http://localhost:8080/h2-console |
| Frontend    | file:///C:/Projekte/Mediaverwaltung/frontend_html/index.html |

### H2 Datenbank-Konfiguration

```
JDBC URL: jdbc:h2:tcp://localhost:9092/C:/Apps/h2/db/mv

Username: sa
Password: (leer)
```

## 📊 Projekt-Status

**Phase 1 (MVP):** ✅ **Abgeschlossen**
- Backend komplett
- Frontend funktionsfähig
- CRUD-Operationen
- CSV/JSON Import

**Phase 2 (In Arbeit):** 🔄 **20% fertig**
- React-Migration (geplant)
- YouTube API Integration (geplant)
- Erweiterte Filter & Suche

## 🧪 Testing

Unit- und Integration-Tests mit JUnit 5:

```bash
cd backend
mvn test
```

## 📝 API Dokumentation

### Endpoints

```
GET    /api/media          - Alle Items abrufen
GET    /api/media/{id}     - Einzelnes Item abrufen
POST   /api/media          - Neues Item erstellen
PUT    /api/media/{id}     - Item aktualisieren
DELETE /api/media/{id}     - Item löschen
GET    /api/media/count    - Anzahl aller Items
```

### Beispiel Request

```bash
# Neues Item erstellen
curl -X POST http://localhost:8080/api/media \
  -H "Content-Type: application/json" \
  -d '{
    "url": "https://youtube.com/watch?v=dQw4w9WgXcQ",
    "beschreibung": "Never Gonna Give You Up",
    "channel": "RickAstleyVEVO",
    "aufrufe": 1500000000,
    "mediaType": "VIDEO"
  }'
```

## 🗺️ Roadmap

- [ ] React Frontend
- [ ] YouTube API Integration (Auto-Fetch Metadaten)
- [ ] PostgreSQL Migration
- [ ] User Authentication
- [ ] Export-Funktion
- [ ] Browser Extension

## 👨‍💻 Entwickler

**Sven** - Java Developer & Informatiker

## 📄 Lizenz

Privates Projekt

---

⭐ **Star this repo if you find it useful!**