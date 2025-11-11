-- MySQL schema aligned with JPA entities
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
DROP TABLE IF EXISTS users;

SET FOREIGN_KEY_CHECKS = 1;

CREATE TABLE users (
  id BIGINT NOT NULL AUTO_INCREMENT,
  username VARCHAR(80) NOT NULL,
  password_hash VARCHAR(255) NOT NULL,
  email VARCHAR(255) NOT NULL,
  display_name VARCHAR(255),
  role VARCHAR(10) NOT NULL,
  enabled TINYINT(1) NOT NULL DEFAULT 1,
  notify_on_message TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  UNIQUE KEY uq_users_username (username),
  UNIQUE KEY uq_users_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE artist (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  bio TEXT,
  image_url VARCHAR(512),
  PRIMARY KEY (id),
  UNIQUE KEY uq_artist_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE genre (
  id BIGINT NOT NULL AUTO_INCREMENT,
  name VARCHAR(120) NOT NULL,
  PRIMARY KEY (id),
  UNIQUE KEY uq_genre_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE album (
  id BIGINT NOT NULL AUTO_INCREMENT,
  primary_artist_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  release_year INT,
  cover_url VARCHAR(512),
  PRIMARY KEY (id),
  CONSTRAINT fk_album_artist FOREIGN KEY (primary_artist_id)
    REFERENCES artist(id)
    ON UPDATE CASCADE ON DELETE RESTRICT,
  UNIQUE KEY uq_album (primary_artist_id, title, release_year),
  KEY ix_album_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE track (
  id BIGINT NOT NULL AUTO_INCREMENT,
  album_id BIGINT NOT NULL,
  title VARCHAR(255) NOT NULL,
  track_no INT,
  is_explicit TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT fk_track_album FOREIGN KEY (album_id)
    REFERENCES album(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE KEY uq_track_number (album_id, track_no),
  KEY ix_track_title (title)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  UNIQUE KEY uq_tf_pair (track_id, artist_id),
  UNIQUE KEY uq_tf_order (track_id, credit_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  UNIQUE KEY uq_track_genre (track_id, genre_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE playlist (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  name VARCHAR(255) NOT NULL,
  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  is_public TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (id),
  CONSTRAINT fk_playlist_user FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  UNIQUE KEY uq_playlist_name (user_id, name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

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
  UNIQUE KEY uq_playlist_position (playlist_id, position)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE listen (
  id BIGINT NOT NULL AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  track_id BIGINT NOT NULL,
  listened_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_listen_user FOREIGN KEY (user_id)
    REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_listen_track FOREIGN KEY (track_id)
    REFERENCES track(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  KEY ix_listen_user (user_id, listened_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE message (
  id BIGINT NOT NULL AUTO_INCREMENT,
  sender_id BIGINT NOT NULL,
  receiver_id BIGINT NOT NULL,
  content TEXT NOT NULL,
  sent_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (id),
  CONSTRAINT fk_msg_sender FOREIGN KEY (sender_id)
    REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  CONSTRAINT fk_msg_receiver FOREIGN KEY (receiver_id)
    REFERENCES users(id)
    ON UPDATE CASCADE ON DELETE CASCADE,
  KEY ix_msg_sender (sender_id, sent_at),
  KEY ix_msg_receiver (receiver_id, sent_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
