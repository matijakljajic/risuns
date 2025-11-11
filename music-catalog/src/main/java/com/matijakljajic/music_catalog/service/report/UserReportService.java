package com.matijakljajic.music_catalog.service.report;

import com.matijakljajic.music_catalog.model.Playlist;
import com.matijakljajic.music_catalog.service.profile.UserProfileService;
import com.matijakljajic.music_catalog.service.profile.UserProfileService.ProfileView;
import com.matijakljajic.music_catalog.service.profile.UserProfileService.TopTrackStat;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserReportService {

  private static final DateTimeFormatter DATE_TIME =
      DateTimeFormatter.ofPattern("d MMM uuuu · HH:mm", Locale.ENGLISH)
          .withZone(ZoneId.systemDefault());
  private static final DateTimeFormatter DAY_STAMP =
      DateTimeFormatter.ofPattern("d MMM uuuu", Locale.ENGLISH)
          .withZone(ZoneId.systemDefault());

  private final UserProfileService userProfiles;
  private final JasperReport compiledTemplate;

  public UserReportService(UserProfileService userProfiles,
                           @Value("classpath:reports/user-profile.jrxml") Resource template) {
    this.userProfiles = userProfiles;
    this.compiledTemplate = compile(template);
  }

  private static JasperReport compile(Resource resource) {
    try (InputStream in = resource.getInputStream()) {
      return JasperCompileManager.compileReport(in);
    } catch (IOException | JRException ex) {
      throw new IllegalStateException("Failed to load Jasper template", ex);
    }
  }

  @Transactional(readOnly = true)
  public byte[] generateReport(Long userId, Authentication authentication) {
    ProfileView view = userProfiles.buildProfile(userId, authentication, 0);
    if (!view.isSelf()) {
      throw new AccessDeniedException("You can only view your own report.");
    }

    Map<String, Object> params = new HashMap<>();
    params.put("ReportGeneratedAt", DATE_TIME.format(Instant.now()));
    params.put("Username", view.getProfile().getUsername());
    params.put("DisplayName", safe(view.getProfile().getDisplayName(), view.getProfile().getUsername()));
    params.put("Email", view.getProfile().getEmail());
    params.put("Role", view.getProfile().getRole().name());
    params.put("PublicPlaylistCount", String.valueOf(view.getPublicPlaylistCount()));
    params.put("PrivatePlaylistCount", String.valueOf(view.getPrivatePlaylistCount()));
    params.put("WeeklyListenCount", String.valueOf(view.getWeeklyListenCount()));
    params.put("WeeklyUniqueTracks", String.valueOf(view.getWeeklyUniqueTracks()));
    params.put("WeeklySince", DAY_STAMP.format(view.getWeeklySince()));
    params.put("TopTracksText", formatTopTracks(view.getTopTracks()));

    List<PlaylistSummary> playlists = view.getVisiblePlaylists().stream()
        .map(UserReportService::toSummary)
        .toList();

    try {
      JasperPrint filled = JasperFillManager.fillReport(
          compiledTemplate,
          params,
          new JRBeanCollectionDataSource(playlists));
      return JasperExportManager.exportReportToPdf(filled);
    } catch (JRException ex) {
      throw new IllegalStateException("Unable to render user report", ex);
    }
  }

  private static PlaylistSummary toSummary(Playlist playlist) {
    return new PlaylistSummary(
        playlist.getName(),
        playlist.isPublic() ? "Public" : "Private",
        playlist.getItems() != null ? playlist.getItems().size() : 0,
        playlist.getCreatedAt() != null ? DAY_STAMP.format(playlist.getCreatedAt()) : "N/A"
    );
  }

  private static String safe(String value, String fallback) {
    return value == null || value.isBlank() ? fallback : value;
  }

  private static String formatTopTracks(List<TopTrackStat> stats) {
    if (stats == null || stats.isEmpty()) {
      return "No listening activity recorded in the last 7 days.";
    }
    return stats.stream()
        .map(stat -> String.format(
            "%s · %s (%d plays)",
            safe(stat.getTrackTitle(), "Unknown track"),
            safe(stat.getAlbumTitle(), "Unknown album"),
            stat.getPlays()))
        .collect(Collectors.joining("\n"));
  }

  @Getter
  @RequiredArgsConstructor
  public static class PlaylistSummary {
    private final String name;
    private final String visibilityLabel;
    private final int trackCount;
    private final String createdAtLabel;
  }
}
