-- Clean tables
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
  (1, 'Nujabes', 'https://lastfm.freetls.fastly.net/i/u/770x0/4ed976c65bb34de89cb92b051e330612.jpg#4ed976c65bb34de89cb92b051e330612', 'Japanski producent koji je spojio hip-hop i džez u prepoznatljiv melankolični zvuk.'),
  (2, 'Shing02', 'https://lastfm.freetls.fastly.net/i/u/770x0/6022ae694247356aebb0fe8f0a733cc2.jpg#6022ae694247356aebb0fe8f0a733cc2', NULL),
  (3, 'Uyama Hiroto', 'https://lastfm.freetls.fastly.net/i/u/770x0/01694eb5af4c8ab3300328457f8329cf.jpg#01694eb5af4c8ab3300328457f8329cf', 'Saksofonista i multiinstrumentalista koji je sa Nujabesom kreirao atmosferične instrumentale.'),
  (4, 'J Dilla', 'https://lastfm.freetls.fastly.net/i/u/770x0/21e2a26aac95496582daafa7528bfb97.jpg#21e2a26aac95496582daafa7528bfb97', NULL),
  (5, 'Fat Jon', 'https://lastfm.freetls.fastly.net/i/u/770x0/e524599ecfc4161def0af4fd1da1c115.jpg#e524599ecfc4161def0af4fd1da1c115', NULL);

-- Albums
INSERT INTO album (id, title, release_year, primary_artist_id, cover_url) VALUES
  (1, 'Modal Soul', 2005, 1, 'https://lastfm.freetls.fastly.net/i/u/500x500/e605e0031a208775b7ac76f8c34290e3.jpg'),
  (2, 'Metaphorical Music', 2003, 1, 'https://lastfm.freetls.fastly.net/i/u/500x500/d0923aa8b7a3fbb2d9b456306750c083.jpg'),
  (3, 'Departure', 2008, 3, 'https://placehold.co/600x600?text=Departure'),
  (4, 'Donuts', 2006, 4, 'https://lastfm.freetls.fastly.net/i/u/500x500/8f5eb95d8de4652a2b8d5b5e26719a22.jpg');

-- Genres
INSERT INTO genre (id, name) VALUES
  (1, 'Hip-Hop'),
  (2, 'Jazz Rap'),
  (3, 'Instrumental'),
  (4, 'Lo-Fi');

-- Tracks
INSERT INTO track (id, album_id, title, track_no, is_explicit) VALUES
  (1, 1, 'Feather', 1, FALSE),
  (2, 1, 'Luv Sic Part 3', 2, FALSE),
  (3, 1, 'Sky Is Tumbling', 5, FALSE),
  (4, 2, 'Arurian Dance', 1, FALSE),
  (5, 2, 'Another Reflection', 3, FALSE),
  (6, 3, '81Summer', 2, FALSE),
  (7, 3, 'Homeward Journey', 5, FALSE),
  (8, 4, 'Workinonit', 1, TRUE),
  (9, 4, 'Last Donut of the Night', 31, FALSE),
  (10, 4, 'Airworks', 5, TRUE);

INSERT INTO track_genre (track_id, genre_id) VALUES
  (1, 1), (1, 2),
  (2, 1),
  (3, 1), (3, 2),
  (4, 2),
  (5, 3),
  (6, 3), (6, 4),
  (7, 4),
  (8, 1),
  (9, 3), (9, 4),
  (10, 1), (10, 3);

INSERT INTO track_feature (id, track_id, artist_id, credit_order) VALUES
  (1, 1, 2, 1),
  (2, 1, 3, 2),
  (3, 2, 2, 1),
  (4, 3, 5, 1);

-- Users (password hash = bcrypt(username))
INSERT INTO users (id, username, email, password_hash, role, enabled, display_name, notify_on_message) VALUES
  (1, 'matija', 'matija@example.rs', '$2a$10$WGzIJ0FB/1ccOV8cdOwtV.I6vWhBoTjEfagoTFcEzciCwgmjavMfe', 'USER', TRUE, 'Matija', TRUE),
  (2, 'jelena4938', 'jelena@example.rs', '$2a$10$nM.20N9Wn9jXdTYAiJuMguYWt.ZVi4w41bqECoEdu4Zdu7fFZkf32', 'USER', TRUE, 'Jelena', TRUE),
  (3, 'nikola3035', 'nikola@example.rs', '$2a$10$Lb0lw18kBYliFlmf07w1duhw2mXGCVKqKd0qvgbCX8Bw5OxBCzjRK', 'USER', TRUE, 'Nikola', FALSE),
  (4, 'admin',  'admin@example.rs',  '$2a$10$KZK0GH0wawI7KqNnTA8HpOmk88buK5J/XcG5hc4uVZoerpKKKbF1S', 'ADMIN', TRUE, 'Admin', FALSE);

-- Messages (Serbian content)
INSERT INTO message (id, sender_id, receiver_id, sent_at, content) VALUES
  (1, 2, 1, CURRENT_TIMESTAMP - INTERVAL '4' HOUR, 'Ćao Matija, slušaš li i dalje Nujabesa?'),
  (2, 1, 2, CURRENT_TIMESTAMP - INTERVAL '3' HOUR, 'Naravno :D'),
  (3, 3, 1, CURRENT_TIMESTAMP - INTERVAL '2' HOUR, 'Ajde podeli novu plejlistu kad stigneš'),
  (4, 1, 3, CURRENT_TIMESTAMP - INTERVAL '90' MINUTE, 'Stiže večeras!');

-- Playlists
INSERT INTO playlist (id, user_id, name, created_at, is_public) VALUES
  (1, 1, 'Vožnja', CURRENT_TIMESTAMP - INTERVAL '2' DAY, FALSE),
  (2, 1, 'Fokus', CURRENT_TIMESTAMP - INTERVAL '26' HOUR, TRUE),
  (3, 2, 'Jutro', CURRENT_TIMESTAMP - INTERVAL '6' HOUR, TRUE),
  (4, 3, 'Noćna', CURRENT_TIMESTAMP - INTERVAL '4' HOUR, TRUE);

INSERT INTO playlist_item (id, playlist_id, track_id, position) VALUES
  (1, 1, 1, 1),
  (2, 1, 2, 2),
  (3, 1, 8, 3),
  (4, 2, 4, 1),
  (5, 2, 5, 2),
  (6, 3, 6, 1),
  (7, 3, 9, 2),
  (8, 4, 8, 1),
  (9, 4, 10, 2);

-- Listens
INSERT INTO listen (id, user_id, track_id, listened_at) VALUES
  (1, 1, 1, CURRENT_TIMESTAMP - INTERVAL '1' HOUR),
  (2, 1, 2, CURRENT_TIMESTAMP - INTERVAL '2' HOUR),
  (3, 1, 8, CURRENT_TIMESTAMP - INTERVAL '3' HOUR),
  (4, 2, 4, CURRENT_TIMESTAMP - INTERVAL '90' MINUTE),
  (5, 2, 6, CURRENT_TIMESTAMP - INTERVAL '30' MINUTE),
  (6, 3, 10, CURRENT_TIMESTAMP - INTERVAL '15' MINUTE);
