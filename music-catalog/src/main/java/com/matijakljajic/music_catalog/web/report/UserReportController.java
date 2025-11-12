package com.matijakljajic.music_catalog.web.report;

import com.matijakljajic.music_catalog.model.Playlist;
import com.matijakljajic.music_catalog.service.profile.UserProfileService;
import com.matijakljajic.music_catalog.service.profile.UserProfileService.ProfileView;
import com.matijakljajic.music_catalog.service.report.UserReportService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

@Controller
@RequestMapping("/users/{userId}/report")
@RequiredArgsConstructor
public class UserReportController {

  private static final DateTimeFormatter DATE_TIME =
      DateTimeFormatter.ofPattern("d MMM uuuu · HH:mm", Locale.ENGLISH)
          .withZone(ZoneId.systemDefault());

  private final UserProfileService userProfiles;
  private final UserReportService reports;

  @GetMapping
  @Transactional(readOnly = true)
  public String report(@PathVariable("userId") Long userId,
                       Authentication authentication,
                       Model model) {
    ProfileView view = userProfiles.buildProfile(userId, authentication, 0);
    ensureSelf(view);

    model.addAttribute("profile", view.getProfile());
    model.addAttribute("publicPlaylistCount", view.getPublicPlaylistCount());
    model.addAttribute("privatePlaylistCount", view.getPrivatePlaylistCount());
    model.addAttribute("weeklyListenCount", view.getWeeklyListenCount());
    model.addAttribute("weeklyUniqueTracks", view.getWeeklyUniqueTracks());
    model.addAttribute("weeklySinceLabel", DATE_TIME.format(view.getWeeklySince()));
    model.addAttribute("topTracks", view.getTopTracks());
    model.addAttribute("visiblePlaylists", view.getVisiblePlaylists());
    model.addAttribute("playlistSummaries", summarize(view.getVisiblePlaylists()));
    model.addAttribute("downloadUrl", "/users/" + userId + "/report/download");
    model.addAttribute("generatedAt", DATE_TIME.format(Instant.now()));
    return "profile/report";
  }

  @GetMapping(value = "/download", produces = MediaType.APPLICATION_PDF_VALUE)
  public ResponseEntity<byte[]> download(@PathVariable("userId") Long userId,
                                         Authentication authentication) {
    byte[] pdf = reports.generateReport(userId, authentication);
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"music-profile-" + userId + ".pdf\"")
        .contentType(MediaType.APPLICATION_PDF)
        .body(pdf);
  }

  private static void ensureSelf(ProfileView view) {
    if (!view.isSelf()) {
      throw new AccessDeniedException("Only the profile owner can access reports.");
    }
  }

  private static List<PlaylistRow> summarize(List<Playlist> playlists) {
    return playlists.stream()
        .map(UserReportController::toRow)
        .collect(Collectors.toList());
  }

  private static PlaylistRow toRow(Playlist playlist) {
    String createdAt = playlist.getCreatedAt() != null
        ? DATE_TIME.format(playlist.getCreatedAt())
        : "N/A";
    int trackCount = playlist.getItems() != null ? playlist.getItems().size() : 0;
    return new PlaylistRow(
        playlist.getId(),
        playlist.getName(),
        playlist.isPublic() ? "Public" : "Private",
        trackCount,
        createdAt
    );
  }

  @Getter
  @RequiredArgsConstructor
  public static class PlaylistRow {
    private final Long id;
    private final String name;
    private final String visibility;
    private final int trackCount;
    private final String createdAtLabel;
  }
}
