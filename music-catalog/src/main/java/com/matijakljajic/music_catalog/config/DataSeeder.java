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
@Profile("seed-jpa")
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
    if (genres.count() > 0) {
      System.out.println("DataSeeder: Already seeded.");
      return;
    }

    var nujabes = artist(
        "Nujabes",
        "https://lastfm.freetls.fastly.net/i/u/770x0/4ed976c65bb34de89cb92b051e330612.jpg#4ed976c65bb34de89cb92b051e330612",
        "Japanski producent koji je spojio hip-hop i džez u prepoznatljiv melankolični zvuk.");
    var shing02 = artist(
        "Shing02",
        "https://lastfm.freetls.fastly.net/i/u/770x0/6022ae694247356aebb0fe8f0a733cc2.jpg#6022ae694247356aebb0fe8f0a733cc2",
        null);
    var uyama = artist(
        "Uyama Hiroto",
        "https://lastfm.freetls.fastly.net/i/u/770x0/01694eb5af4c8ab3300328457f8329cf.jpg#01694eb5af4c8ab3300328457f8329cf",
        "Saksofonista i multiinstrumentalista koji je sa Nujabesom kreirao atmosferične instrumentale.");
    var jDilla = artist(
        "J Dilla",
        "https://lastfm.freetls.fastly.net/i/u/770x0/21e2a26aac95496582daafa7528bfb97.jpg#21e2a26aac95496582daafa7528bfb97",
        null);
    var fatJon = artist(
        "Fat Jon",
        "https://lastfm.freetls.fastly.net/i/u/770x0/e524599ecfc4161def0af4fd1da1c115.jpg#e524599ecfc4161def0af4fd1da1c115",
        null);
    artists.saveAll(List.of(nujabes, shing02, uyama, jDilla, fatJon));

    var hipHop = genre("Hip-Hop");
    var jazzRap = genre("Jazz Rap");
    var instrumental = genre("Instrumental");
    var loFi = genre("Lo-Fi");
    genres.saveAll(List.of(hipHop, jazzRap, instrumental, loFi));

    var modalSoul = album(nujabes, "Modal Soul", 2005, "https://lastfm.freetls.fastly.net/i/u/500x500/e605e0031a208775b7ac76f8c34290e3.jpg");
    var metaphoricalMusic = album(nujabes, "Metaphorical Music", 2003, "https://lastfm.freetls.fastly.net/i/u/500x500/d0923aa8b7a3fbb2d9b456306750c083.jpg");
    var departure = album(uyama, "Departure", 2008, "https://placehold.co/600x600?text=Departure");
    var donuts = album(jDilla, "Donuts", 2006, "https://lastfm.freetls.fastly.net/i/u/500x500/8f5eb95d8de4652a2b8d5b5e26719a22.jpg");
    albums.saveAll(List.of(modalSoul, metaphoricalMusic, departure, donuts));

    var feather = track(modalSoul, "Feather", 1, false, hipHop, jazzRap);
    var luvSic3 = track(modalSoul, "Luv Sic Part 3", 2, false, hipHop);
    var skyIsTumbling = track(modalSoul, "Sky Is Tumbling", 5, false, hipHop, jazzRap);
    var arurianDance = track(metaphoricalMusic, "Arurian Dance", 1, false, jazzRap);
    var anotherReflection = track(metaphoricalMusic, "Another Reflection", 3, false, instrumental);
    var eightyOneSummer = track(departure, "81Summer", 2, false, loFi, instrumental);
    var homeWardJourney = track(departure, "Homeward Journey", 5, false, loFi);
    var workinonit = track(donuts, "Workinonit", 1, true, hipHop);
    var lastDonut = track(donuts, "Last Donut of the Night", 31, false, instrumental, loFi);
    var airworks = track(donuts, "Airworks", 5, true, hipHop, instrumental);
    tracks.saveAll(List.of(
        feather, luvSic3, skyIsTumbling, arurianDance, anotherReflection,
        eightyOneSummer, homeWardJourney, workinonit, lastDonut, airworks
    ));

    features.saveAll(List.of(
        feature(feather, shing02, 1),
        feature(feather, uyama, 2),
        feature(luvSic3, shing02, 1),
        feature(skyIsTumbling, fatJon, 1)
    ));

    var matija = user("matija", "matija@example.rs", "Matija", Role.USER, true);
    var jelena = user("jelena3797", "jelena@example.rs", "Jelena", Role.USER, true);
    var nikola = user("nikola8325", "nikola@example.rs", "Nikola", Role.USER, false);
    var admin = user("admin", "admin@example.rs", "Admin", Role.ADMIN, false);
    users.saveAll(List.of(matija, jelena, nikola, admin));

    var driving = playlist(matija, "Vožnja", false, Instant.now().minus(2, ChronoUnit.DAYS));
    driving.addTrackToEnd(feather);
    driving.addTrackToEnd(luvSic3);
    driving.addTrackToEnd(workinonit);

    var focus = playlist(matija, "Fokus", true, Instant.now().minus(26, ChronoUnit.HOURS));
    focus.addTrackToEnd(arurianDance);
    focus.addTrackToEnd(anotherReflection);

    var morning = playlist(jelena, "Jutro", true, Instant.now().minus(6, ChronoUnit.HOURS));
    morning.addTrackToEnd(eightyOneSummer);
    morning.addTrackToEnd(lastDonut);

    var nightVibes = playlist(nikola, "Noćna", true, Instant.now().minus(4, ChronoUnit.HOURS));
    nightVibes.addTrackToEnd(workinonit);
    nightVibes.addTrackToEnd(airworks);

    playlists.saveAll(List.of(driving, focus, morning, nightVibes));

    listens.saveAll(List.of(
        Listen.of(matija, feather, Instant.now().minus(1, ChronoUnit.HOURS)),
        Listen.of(matija, luvSic3, Instant.now().minus(2, ChronoUnit.HOURS)),
        Listen.of(matija, workinonit, Instant.now().minus(3, ChronoUnit.HOURS)),
        Listen.of(jelena, arurianDance, Instant.now().minus(90, ChronoUnit.MINUTES)),
        Listen.of(jelena, eightyOneSummer, Instant.now().minus(30, ChronoUnit.MINUTES)),
        Listen.of(nikola, airworks, Instant.now().minus(15, ChronoUnit.MINUTES))
    ));

    messages.saveAll(List.of(
        Message.of(jelena, matija, "Ćao, slušaš li i dalje Nujabesa?"),
        Message.of(matija, jelena, "Naravno :D"),
        Message.of(nikola, matija, "Ajde podeli novu plejlistu"),
        Message.of(matija, nikola, "Stiže večeras!")
    ));
  }

  private static Artist artist(String name, String imageUrl, String bio) {
    var a = new Artist();
    a.setName(name);
    a.setImageUrl(imageUrl);
    a.setBio(bio);
    return a;
  }

  private static Album album(Artist primary, String title, int year, String coverUrl) {
    var album = new Album();
    album.setPrimaryArtist(primary);
    album.setTitle(title);
    album.setReleaseYear(year);
    album.setCoverUrl(coverUrl);
    return album;
  }

  private static Track track(Album album, String title, int trackNo, boolean explicit, Genre... assignedGenres) {
    var track = new Track();
    track.setAlbum(album);
    track.setTitle(title);
    track.setTrackNo(trackNo);
    track.setExplicit(explicit);
    for (Genre genre : assignedGenres) {
      track.getGenres().add(genre);
    }
    return track;
  }

  private static TrackFeature feature(Track track, Artist artist, int order) {
    var tf = new TrackFeature();
    tf.setTrack(track);
    tf.setArtist(artist);
    tf.setCreditOrder(order);
    return tf;
  }

  private User user(String username, String email, String displayName, Role role, boolean notify) {
    var u = new User();
    u.setUsername(username);
    u.setEmail(email);
    u.setDisplayName(displayName);
    u.setRole(role);
    u.setPasswordHash(passwordEncoder.encode(username));
    u.setEnabled(true);
    u.setNotifyOnMessage(notify);
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
