package com.matijakljajic.music_catalog;

import com.matijakljajic.music_catalog.model.Role;
import com.matijakljajic.music_catalog.service.catalog.GenreBrowseService;
import com.matijakljajic.music_catalog.service.listening.RecentListenService;
import com.matijakljajic.music_catalog.service.search.SearchService;
import com.matijakljajic.music_catalog.service.search.SearchService.SearchResults;
import com.matijakljajic.music_catalog.service.search.SearchService.TrackResult;
import com.matijakljajic.music_catalog.service.search.SearchService.ArtistResult;
import com.matijakljajic.music_catalog.service.search.SearchService.AlbumResult;
import com.matijakljajic.music_catalog.service.search.SearchService.PlaylistResult;
import com.matijakljajic.music_catalog.service.search.SearchService.UserResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

  private final RecentListenService recentListens;
  private final GenreBrowseService genres;
  private final SearchService searchService;

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("recentListens", recentListens.latest(10));
    model.addAttribute("genres", genres.allGenres());
    return "home";
  }

  @GetMapping("/search")
  public String search(@RequestParam(value = "q", required = false) String query,
                       @RequestParam(value = "genreId", required = false) Long genreId,
                       @RequestParam(value = "type", defaultValue = "general") String type,
                       @RequestParam(value = "explicit", required = false) String explicitParam,
                       @RequestParam(value = "releaseYear", required = false) Integer releaseYear,
                       @RequestParam(value = "role", required = false) String roleParam,
                       Model model) {
    String searchType = normalizeType(type);
    Boolean explicitFilter = parseBooleanParam(explicitParam);
    Role roleFilter = parseRoleParam(roleParam);

    boolean hasQuery = query != null && !query.isBlank();
    boolean hasFilters = switch (searchType) {
      case "tracks" -> hasQuery || genreId != null || explicitFilter != null;
      case "albums" -> hasQuery || releaseYear != null;
      case "users" -> hasQuery || roleFilter != null;
      case "artists" -> hasQuery;
      default -> hasQuery;
    };

    List<TrackResult> trackResults = List.of();
    List<ArtistResult> artistResults = List.of();
    List<AlbumResult> albumResults = List.of();
    List<PlaylistResult> playlistResults = List.of();
    List<UserResult> userResults = List.of();

    switch (searchType) {
      case "tracks" -> trackResults = hasFilters
          ? searchService.searchTracks(query, genreId, explicitFilter)
          : List.of();
      case "artists" -> artistResults = hasFilters ? searchService.searchArtists(query) : List.of();
      case "albums" -> albumResults = hasFilters ? searchService.searchAlbums(query, releaseYear) : List.of();
      case "users" -> userResults = hasFilters ? searchService.searchUsers(query, roleFilter, null) : List.of();
      default -> {
        SearchResults results = hasFilters ? searchService.searchGeneral(query) : SearchResults.empty();
        trackResults = results.getTrackMatches();
        artistResults = results.getArtistMatches();
        albumResults = results.getAlbumMatches();
        playlistResults = results.getPlaylistMatches();
        userResults = results.getUserMatches();
      }
    }

    boolean noResults = hasFilters &&
        trackResults.isEmpty() &&
        artistResults.isEmpty() &&
        albumResults.isEmpty() &&
        playlistResults.isEmpty() &&
        userResults.isEmpty();

    model.addAttribute("query", query);
    model.addAttribute("selectedGenreId", genreId);
    model.addAttribute("searchType", searchType);
    model.addAttribute("genres", "tracks".equals(searchType) ? genres.allGenres() : List.of());
    model.addAttribute("roles", Role.values());
    model.addAttribute("explicitFilter", valueOrEmpty(explicitParam));
    model.addAttribute("releaseYear", releaseYear);
    model.addAttribute("roleFilter", roleFilter);
    model.addAttribute("searched", hasFilters);
    model.addAttribute("noResults", noResults);

    model.addAttribute("trackResults", trackResults);
    model.addAttribute("artistResults", artistResults);
    model.addAttribute("albumResults", albumResults);
    model.addAttribute("playlistResults", playlistResults);
    model.addAttribute("userResults", userResults);

    return "search";
  }

  // --- Diagnostic endpoint ---
  @GetMapping("/ping")
  @ResponseBody
  public String ping() {
    return "ok";
  }

  @GetMapping("/search/filter-data")
  @ResponseBody
  public FilterDataResponse filterData(@RequestParam("type") String type) {
    String searchType = normalizeType(type);
    if ("tracks".equals(searchType)) {
      var genreOptions = genres.allGenres().stream()
          .map(g -> new GenreOption(g.getId(), g.getName()))
          .toList();
      return new FilterDataResponse(searchType, genreOptions);
    }
    return new FilterDataResponse(searchType, List.of());
  }

  private String normalizeType(String type) {
    if (type == null || type.isBlank()) return "general";
    return switch (type.toLowerCase()) {
      case "tracks", "artists", "albums", "users" -> type.toLowerCase();
      default -> "general";
    };
  }

  private Boolean parseBooleanParam(String value) {
    if (value == null || value.isBlank()) return null;
    if ("true".equalsIgnoreCase(value)) return Boolean.TRUE;
    if ("false".equalsIgnoreCase(value)) return Boolean.FALSE;
    return null;
  }

  private String valueOrEmpty(String value) {
    return value == null ? "" : value;
  }

  private Role parseRoleParam(String value) {
    if (value == null || value.isBlank()) return null;
    try {
      return Role.valueOf(value.toUpperCase());
    } catch (IllegalArgumentException ex) {
      return null;
    }
  }

  public record FilterDataResponse(String type, List<GenreOption> genres) { }

  public record GenreOption(Long id, String name) { }
}
