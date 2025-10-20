-- ============================================================================
-- PostgreSQL Database Schema for MediaVerwaltung
-- ============================================================================
-- Version: 1.0
-- Date: 2025-10-20
-- Database: PostgreSQL 12+
-- ============================================================================

-- Drop table if exists (for clean re-creation)
DROP TABLE IF EXISTS media_item CASCADE;

-- ============================================================================
-- Table: media_item
-- ============================================================================
-- Stores all media items (videos, audio, text articles)
-- ============================================================================

CREATE TABLE media_item (
    -- Primary Key (auto-increment via BIGSERIAL)
    id BIGSERIAL NOT NULL,
    
    -- Timestamp (auto-set on INSERT, PostgreSQL uses TIMESTAMP)
    last_updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    
    -- Required: URL to the media source
    url VARCHAR(1000) NOT NULL,
    
    -- Optional: Description/Title
    beschreibung VARCHAR(1000),
    
    -- Optional: Channel/Creator name
    channel VARCHAR(255),
    
    -- Optional: Duration (format: HH:MM:SS or MM:SS)
    dauer VARCHAR(255),
    
    -- Optional: Keywords/Tags (comma-separated for now)
    stichwort VARCHAR(255),
    
    -- Optional: Media type (VIDEO, AUDIO, TEXT)
    media_type VARCHAR(255),
    
    -- Constraints
    CONSTRAINT pk_media_item PRIMARY KEY (id)
);

-- ============================================================================
-- Indexes (for performance)
-- ============================================================================

-- Index on media_type for fast filtering
CREATE INDEX idx_media_type ON media_item(media_type);

-- Index on channel for fast filtering
CREATE INDEX idx_channel ON media_item(channel);

-- Index on last_updated_at for sorting by date
CREATE INDEX idx_last_updated_at ON media_item(last_updated_at DESC);

-- ============================================================================
-- Comments (PostgreSQL supports table and column comments)
-- ============================================================================

COMMENT ON TABLE media_item IS 'Stores all media items (videos, audio, text)';
COMMENT ON COLUMN media_item.id IS 'Primary key, auto-increment';
COMMENT ON COLUMN media_item.last_updated_at IS 'Last modification timestamp';
COMMENT ON COLUMN media_item.url IS 'Source URL (required field)';
COMMENT ON COLUMN media_item.beschreibung IS 'Description or title';
COMMENT ON COLUMN media_item.channel IS 'Channel or creator name';
COMMENT ON COLUMN media_item.dauer IS 'Duration (e.g. 00:15:30)';
COMMENT ON COLUMN media_item.stichwort IS 'Keywords/Tags';
COMMENT ON COLUMN media_item.media_type IS 'Type: VIDEO, AUDIO, TEXT';

-- ============================================================================
-- Sample Data (optional, for testing)
-- ============================================================================

INSERT INTO media_item (url, beschreibung, channel, dauer, stichwort, media_type) VALUES
('https://youtube.com/watch?v=dQw4w9WgXcQ', 'Never Gonna Give You Up', 'Rick Astley', '00:03:33', 'music,80s', 'VIDEO'),
('https://youtube.com/watch?v=9bZkp7q19f0', 'Gangnam Style', 'PSY', '00:04:13', 'kpop,viral', 'VIDEO'),
('https://spotify.com/episode/example', 'Tech Talk Episode 1', 'TechPodcast', '00:45:00', 'tech,podcast', 'AUDIO'),
('https://medium.com/@user/ai-article', 'Understanding AI in 2025', 'AI Weekly', NULL, 'ai,tech', 'TEXT');

-- ============================================================================
-- Additional PostgreSQL-specific features (optional)
-- ============================================================================

-- Create a function to auto-update last_updated_at on UPDATE
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.last_updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- Create trigger to auto-update timestamp
CREATE TRIGGER update_media_item_modtime
    BEFORE UPDATE ON media_item
    FOR EACH ROW
    EXECUTE FUNCTION update_modified_column();

-- ============================================================================
-- End of Schema
-- ============================================================================
