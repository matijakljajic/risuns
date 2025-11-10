-- Clean tables (order matters due to FKs)
DELETE FROM playlist_item;
DELETE FROM playlist;
DELETE FROM message;
DELETE FROM listen;
DELETE FROM track_feature;
DELETE FROM track_genre;
DELETE FROM track;
DELETE FROM album;
DELETE FROM artist;
DELETE FROM genre;
DELETE FROM users;

-- Artists
INSERT INTO artist (id, name, image_url, bio) VALUES
  (1, 'Nujabes', NULL, NULL),
  (2, 'Shing02', NULL, NULL),
  (3, 'Uyama Hiroto', NULL, NULL);

-- Albums
INSERT INTO album (id, title, release_year, primary_artist_id, cover_url) VALUES
  (1, 'Modal Soul', 2005, 1, NULL),
  (2, 'Metaphorical Music', 2003, 1, NULL);

-- Tracks
INSERT INTO track (id, album_id, title, track_no, is_explicit) VALUES
  (1, 1, 'Feather', 1, FALSE),
  (2, 1, 'Luv Sic Part 3', 2, FALSE),
  (3, 2, 'Arurian Dance', 1, FALSE);

-- Genres
INSERT INTO genre (id, name) VALUES
  (1, 'Hip-Hop'),
  (2, 'Jazz Rap');

-- Track-Genres (many-to-many)
INSERT INTO track_genre (track_id, genre_id) VALUES
  (1, 1),
  (1, 2);

-- Featured artists for 'Feather' (ordered by credit_order)
INSERT INTO track_feature (id, track_id, artist_id, credit_order) VALUES
  (1, 1, 2, 1), -- Shing02
  (2, 1, 3, 2); -- Uyama Hiroto

-- Users
INSERT INTO users (id, username, email, password_hash, role, enabled, display_name) VALUES
  (1, 'matija', 'matija@example.test', '$2a$12$MLMQq56IrBCUNeOrvMIc4ewHt6U1EV8HftSZv93A7ZJjeKiK7n9C6', 'USER', TRUE, 'Matija'),
  (2, 'sara',    'nuj@example.test',    '$2a$12$ZEB97SJORmPRJDlWA1NrP.SP.FnETf8aO/VcKqIrPGwyBCKqKsdNW', 'USER', TRUE, 'Sara'),
  (3, 'admin',  'admin@example.test',  '$2a$12$XIUODmY6BvfR2ygtH5z4u.3g6967xzlYwhP9KMzPauzO.ywNRYwMO', 'ADMIN', TRUE, 'Admin');

-- Messages
INSERT INTO message (id, sender_id, receiver_id, sent_at, content) VALUES
  (1, 2, 1, CURRENT_TIMESTAMP, 'Hey there!'),
  (2, 1, 2, CURRENT_TIMESTAMP, 'Hi!');

-- Playlists
INSERT INTO playlist (id, user_id, name, created_at, is_public) VALUES
  (1, 1, 'Driving', CURRENT_TIMESTAMP - INTERVAL '2' DAY, FALSE),
  (2, 1, 'Study',   CURRENT_TIMESTAMP - INTERVAL '1' DAY, TRUE);

-- Playlist items
INSERT INTO playlist_item (id, playlist_id, track_id, position) VALUES
  (1, 1, 1, 1), -- Driving: Feather
  (2, 1, 2, 2), -- Driving: Luv Sic Part 3
  (3, 2, 3, 1); -- Study: Arurian Dance

-- Listens
INSERT INTO listen (id, user_id, track_id, listened_at) VALUES
  (1, 1, 1, CURRENT_TIMESTAMP - INTERVAL '1' HOUR);
