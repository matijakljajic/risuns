-- schema.sql (MySQL)
-- Charset/engine + safe drops
SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

DROP TABLE IF EXISTS playlist_item;
DROP TABLE IF EXISTS track_feature;
DROP TABLE IF EXISTS track_genre;
DROP TABLE IF EXISTS listen;
DROP TABLE IF EXISTS message;
DROP TABLE IF EXISTS playlist;
DROP TABLE IF EXISTS track;
DROP TABLE IF EXISTS album;
DROP TABLE IF EXISTS genre;
DROP TABLE IF EXISTS artist;
DROP TABLE IF EXISTS app_user;

SET FOREIGN_KEY_CHECKS = 1;

-- Core reference tables
CREATE TABLE artist (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_artist_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE genre (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(100) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_genre_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Use app_user to avoid reserved names
CREATE TABLE app_user (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(100) NOT NULL,
  display_name VARCHAR(255),
  email VARCHAR(255),
  PRIMARY KEY (id),
  UNIQUE KEY uq_user_username (username),
  UNIQUE KEY uq_user_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE album (
  id BIGINT NOT NULL AUTO_INCREMENT,
  artist_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  year INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_album_artist FOREIGN KEY (artist_id)
    REFERENCES artist(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  KEY ix_album_artist (artist_id),
  UNIQUE KEY uq_album_unique (artist_id, title, year)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE track (
  id BIGINT NOT NULL AUTO_INCREMENT,
  album_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  track_number INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_track_album FOREIGN KEY (album_id)
    REFERENCES album(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  KEY ix_track_album (album_id),
  UNIQUE KEY uq_track_num (album_id, track_number)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Featured artists on a track; now with surrogate PK + clear uniques
CREATE TABLE track_feature (
  id BIGINT NOT NULL AUTO_INCREMENT,
  track_id BIGINT NOT NULL,
  artist_id BIGINT NOT NULL,
  credit_order INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_tf_track FOREIGN KEY (track_id)
    REFERENCES track(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_tf_artist FOREIGN KEY (artist_id)
    REFERENCES artist(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE KEY uq_feature_pair (track_id, artist_id),
  UNIQUE KEY uq_feature_order (track_id, credit_order),
  KEY ix_tf_order (track_id, credit_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Track ↔ Genre (keep surrogate PK for consistency)
CREATE TABLE track_genre (
  id BIGINT NOT NULL AUTO_INCREMENT,
  track_id BIGINT NOT NULL,
  genre_id BIGINT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_tg_track FOREIGN KEY (track_id)
    REFERENCES track(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_tg_genre FOREIGN KEY (genre_id)
    REFERENCES genre(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE KEY uq_track_genre (track_id, genre_id),
  KEY ix_tg_track (track_id),
  KEY ix_tg_genre (genre_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE playlist (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  is_public TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT fk_playlist_user FOREIGN KEY (user_id)
    REFERENCES app_user(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE KEY uq_playlist_owner_name (user_id, name),
  KEY ix_playlist_user (user_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Playlist items; surrogate PK + two uniques (pair + position)
CREATE TABLE playlist_item (
  id BIGINT NOT NULL AUTO_INCREMENT,
  playlist_id BIGINT NOT NULL,
  track_id BIGINT NOT NULL,
  position INT NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT fk_pi_playlist FOREIGN KEY (playlist_id)
    REFERENCES playlist(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_pi_track FOREIGN KEY (track_id)
    REFERENCES track(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE KEY uq_playlist_track (playlist_id, track_id),
  UNIQUE KEY uq_playlist_position (playlist_id, position),
  KEY ix_pi_playlist_pos (playlist_id, position)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE listen (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  track_id BIGINT NOT NULL,
  listened_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_listen_user FOREIGN KEY (user_id)
    REFERENCES app_user(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_listen_track FOREIGN KEY (track_id)
    REFERENCES track(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  KEY ix_listen_user_time (user_id, listened_at),
  KEY ix_listen_track_time (track_id, listened_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE message (
  id BIGINT NOT NULL AUTO_INCREMENT,
  sender_id BIGINT NOT NULL,
  recipient_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_msg_sender FOREIGN KEY (sender_id)
    REFERENCES app_user(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_msg_recipient FOREIGN KEY (recipient_id)
    REFERENCES app_user(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  KEY ix_msg_sender_time (sender_id, sent_at),
  KEY ix_msg_recipient_time (recipient_id, sent_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
