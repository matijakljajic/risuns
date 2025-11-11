package com.matijakljajic.music_catalog.service.profile;

import com.matijakljajic.music_catalog.model.Listen;
import com.matijakljajic.music_catalog.model.Playlist;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.ListenRepository;
import com.matijakljajic.music_catalog.repository.PlaylistRepository;
import com.matijakljajic.music_catalog.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserProfileService {

  private static final int LISTENS_PAGE_SIZE = 10;
  private static final DateTimeFormatter TIMELINE_FORMAT =
      DateTimeFormatter.ofPattern("d MMM uuuu · HH:mm").withZone(ZoneId.systemDefault());

  private final UserRepository users;
  private final PlaylistRepository playlists;
  private final ListenRepository listens;

  public ProfileView buildProfile(Long userId, Authentication authentication, int page) {
    User user = users.findById(userId).orElseThrow();
    boolean viewingOwnProfile = authentication != null
        && authentication.isAuthenticated()
        && authentication.getName().equalsIgnoreCase(user.getUsername());

    List<Playlist> allPlaylists = playlists.findByUserId(userId);
    List<Playlist> visiblePlaylists = viewingOwnProfile
        ? allPlaylists
        : allPlaylists.stream().filter(Playlist::isPublic).toList();

    long publicPlaylistCount = allPlaylists.stream().filter(Playlist::isPublic).count();
    long privatePlaylistCount = allPlaylists.size() - publicPlaylistCount;

    Instant weekAgo = Instant.now().minus(7, ChronoUnit.DAYS);
    var weeklyListens = listens.findByUserIdAndListenedAtGreaterThanEqualOrderByListenedAtDesc(userId, weekAgo);
    long weeklyUniqueTracks = weeklyListens.stream()
        .map(l -> l.getTrack().getId())
        .distinct()
        .count();

    int safePage = Math.max(page, 0);
    int total = weeklyListens.size();
    int fromIndex = Math.min(safePage * LISTENS_PAGE_SIZE, total);
    int toIndex = Math.min(fromIndex + LISTENS_PAGE_SIZE, total);
    var pagedListens = weeklyListens.subList(fromIndex, toIndex).stream()
        .map(l -> new ListenView(l.getTrack(), TIMELINE_FORMAT.format(l.getListenedAt())))
        .toList();
    boolean hasNext = toIndex < total;
    boolean hasPrev = safePage > 0;
    int pageStart = total == 0 ? 0 : fromIndex + 1;
    int pageEnd = toIndex;

    Map<Long, Track> trackLookup = new LinkedHashMap<>();
    weeklyListens.stream()
        .map(Listen::getTrack)
        .filter(track -> track != null && track.getId() != null)
        .forEach(track -> trackLookup.putIfAbsent(track.getId(), track));

    List<TopTrackStat> topTracks = weeklyListens.stream()
        .filter(l -> l.getTrack() != null && l.getTrack().getId() != null)
        .collect(Collectors.groupingBy(l -> l.getTrack().getId(), Collectors.counting()))
        .entrySet().stream()
        .sorted(Map.Entry.<Long, Long>comparingByValue(Comparator.reverseOrder()))
        .limit(5)
        .map(e -> {
          Track track = trackLookup.get(e.getKey());
          var album = track != null ? track.getAlbum() : null;
          Long albumId = album != null ? album.getId() : null;
          String albumTitle = album != null ? album.getTitle() : null;
          String title = track != null ? track.getTitle() : null;
          return new TopTrackStat(
              e.getKey(),
              title,
              albumId,
              albumTitle,
              e.getValue());
        })
        .toList();

    return new ProfileView(
        user,
        visiblePlaylists,
        viewingOwnProfile,
        publicPlaylistCount,
        privatePlaylistCount,
        pagedListens,
        total,
        weeklyUniqueTracks,
        weekAgo,
        safePage,
        hasNext,
        hasPrev,
        pageStart,
        pageEnd,
        topTracks
    );
  }

  @Getter
  @AllArgsConstructor
  public static class ProfileView {
    private final User profile;
    private final List<Playlist> visiblePlaylists;
    private final boolean self;
    private final long publicPlaylistCount;
    private final long privatePlaylistCount;
    private final List<ListenView> weeklyListensPage;
    private final int weeklyListenCount;
    private final long weeklyUniqueTracks;
    private final Instant weeklySince;
    private final int page;
    private final boolean hasNext;
    private final boolean hasPrev;
    private final int pageStart;
    private final int pageEnd;
    private final List<TopTrackStat> topTracks;
  }

  @Getter
  @AllArgsConstructor
  public static class TopTrackStat {
    private final Long trackId;
    private final String trackTitle;
    private final Long albumId;
    private final String albumTitle;
    private final Long plays;
  }

  @Getter
  @AllArgsConstructor
  public static class ListenView {
    private final Track track;
    private final String formattedTime;
  }
}
