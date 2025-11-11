package com.matijakljajic.music_catalog.config;

import com.matijakljajic.music_catalog.model.*;
import com.matijakljajic.music_catalog.repository.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Profile("!h2")
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

  private final ArtistRepository artists;
  private final AlbumRepository albums;
  private final TrackRepository tracks;
  private final TrackFeatureRepository features;
  private final GenreRepository genres;
  private final UserRepository users;
  private final PlaylistRepository playlists;
  private final ListenRepository listens;
  private final MessageRepository messages;
  private final PasswordEncoder passwordEncoder;

  @Override
  @Transactional
  public void run(String... args) {
    if (users.count() > 0) {
      return; // assume already seeded
    }

    var nujabes = artist("Nujabes");
    var shing02 = artist("Shing02");
    var uyama = artist("Uyama Hiroto");
    artists.saveAll(List.of(nujabes, shing02, uyama));

    var hipHop = genre("Hip-Hop");
    var jazzRap = genre("Jazz Rap");
    genres.saveAll(List.of(hipHop, jazzRap));

    var modalSoul = album(nujabes, "Modal Soul", 2005);
    var metaphoricalMusic = album(nujabes, "Metaphorical Music", 2003);
    albums.saveAll(List.of(modalSoul, metaphoricalMusic));

    var feather = track(modalSoul, "Feather", 1, false, List.of(hipHop, jazzRap));
    var luvSic3 = track(modalSoul, "Luv Sic Part 3", 2, false, List.of(hipHop));
    var arurianDance = track(metaphoricalMusic, "Arurian Dance", 1, false, List.of(jazzRap));
    tracks.saveAll(List.of(feather, luvSic3, arurianDance));

    features.saveAll(List.of(
        feature(feather, shing02, 1),
        feature(feather, uyama, 2)
    ));

    var matija = user("matija", "matija@example.test", "Matija", Role.USER);
    var sara = user("sara", "sara@example.test", "Sara", Role.USER);
    var admin = user("admin", "admin@example.test", "Admin", Role.ADMIN);
    users.saveAll(List.of(matija, sara, admin));

    var driving = playlist(matija, "Driving", false, Instant.now().minus(2, ChronoUnit.DAYS));
    driving.addTrackToEnd(feather);
    driving.addTrackToEnd(luvSic3);

    var study = playlist(matija, "Study", true, Instant.now().minus(1, ChronoUnit.DAYS));
    study.addTrackToEnd(arurianDance);
    playlists.saveAll(List.of(driving, study));

    listens.saveAll(List.of(
        Listen.of(matija, feather, Instant.now().minus(1, ChronoUnit.HOURS)),
        Listen.of(matija, luvSic3, Instant.now().minus(2, ChronoUnit.HOURS)),
        Listen.of(sara, arurianDance, Instant.now().minus(90, ChronoUnit.MINUTES))
    ));

    messages.saveAll(List.of(
        Message.of(sara, matija, "Hey there!"),
        Message.of(matija, sara, "Hi!")
    ));
  }

  private static Artist artist(String name) {
    var a = new Artist();
    a.setName(name);
    return a;
  }

  private static Album album(Artist primary, String title, int year) {
    var album = new Album();
    album.setPrimaryArtist(primary);
    album.setTitle(title);
    album.setReleaseYear(year);
    return album;
  }

  private static Track track(Album album, String title, int trackNo, boolean explicit, List<Genre> genreList) {
    var track = new Track();
    track.setAlbum(album);
    track.setTitle(title);
    track.setTrackNo(trackNo);
    track.setExplicit(explicit);
    genreList.forEach(track.getGenres()::add);
    return track;
  }

  private static TrackFeature feature(Track track, Artist artist, int order) {
    var tf = new TrackFeature();
    tf.setTrack(track);
    tf.setArtist(artist);
    tf.setCreditOrder(order);
    return tf;
  }

  private User user(String username, String email, String displayName, Role role) {
    var u = new User();
    u.setUsername(username);
    u.setEmail(email);
    u.setDisplayName(displayName);
    u.setRole(role);
    u.setPasswordHash(passwordEncoder.encode(username + "123!"));
    u.setEnabled(true);
    return u;
  }

  private static Playlist playlist(User owner, String name, boolean isPublic, Instant createdAt) {
    var pl = new Playlist();
    pl.setUser(owner);
    pl.setName(name);
    pl.setPublic(isPublic);
    pl.setCreatedAt(createdAt);
    return pl;
  }

  private static Genre genre(String name) {
    var g = new Genre();
    g.setName(name);
    return g;
  }
}
