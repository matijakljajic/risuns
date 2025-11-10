-- data.sql for MySQL
-- Insert reference data in dependency order so IDs are predictable (start at 1)

-- Artists
INSERT INTO artist (name) VALUES
  ('Daft Punk'),
  ('Pharrell Williams'),
  ('Nile Rodgers'),
  ('Kanye West');

-- Genres
INSERT INTO genre (name) VALUES
  ('Electronic'), ('Disco'), ('Pop'), ('Hip-Hop');

-- Users
INSERT INTO app_user (username, display_name, email) VALUES
  ('alice', 'Alice', 'alice@example.com'),
  ('bob',   'Bob',   'bob@example.com');

-- Albums
-- 1: Daft Punk, 2: Pharrell, 3: Nile, 4: Kanye (per inserts above)
INSERT INTO album (artist_id, title, year) VALUES
  (1, 'Random Access Memories', 2013),
  (4, 'Graduation',             2007);

-- Tracks
-- Assume track IDs: 1 = Get Lucky, 2 = Stronger
INSERT INTO track (album_id, title, track_number) VALUES
  (1, 'Get Lucky', 1),
  (2, 'Stronger',  2);

-- Track features
-- Get Lucky features Pharrell (order 1) and Nile (order 2)
INSERT INTO track_feature (track_id, artist_id, credit_order) VALUES
  (1, 2, 1),
  (1, 3, 2);
-- Stronger features Daft Punk
INSERT INTO track_feature (track_id, artist_id, credit_order) VALUES
  (2, 1, 1);

-- Track genres
-- Get Lucky: Electronic, Disco, Pop
INSERT INTO track_genre (track_id, genre_id) VALUES
  (1, 1), (1, 2), (1, 3);
-- Stronger: Hip-Hop, Electronic
INSERT INTO track_genre (track_id, genre_id) VALUES
  (2, 4), (2, 1);

-- Playlists
INSERT INTO playlist (user_id, name, is_public) VALUES
  (1, 'Alice Favorites', 1);

-- Playlist items (positions unique per playlist)
INSERT INTO playlist_item (playlist_id, track_id, position) VALUES
  (1, 1, 1),
  (1, 2, 2);

-- Listen history
INSERT INTO listen (user_id, track_id, listened_at) VALUES
  (1, 1, CURRENT_TIMESTAMP),
  (1, 2, CURRENT_TIMESTAMP),
  (2, 1, CURRENT_TIMESTAMP);

-- Messages
INSERT INTO message (sender_id, recipient_id, content, sent_at) VALUES
  (1, 2, 'Hey Bob, check out “Get Lucky”!', CURRENT_TIMESTAMP),
  (2, 1, 'Nice! Also try “Stronger”.',       CURRENT_TIMESTAMP);
