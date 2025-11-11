package com.matijakljajic.music_catalog.web;

import com.matijakljajic.music_catalog.service.library.UserPlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/playlists")
public class PlaylistActionsController {

  private final UserPlaylistService userPlaylists;

  @PostMapping("/{playlistId}/items/{itemId}/move")
  public String moveItem(@PathVariable Long playlistId,
                         @PathVariable Long itemId,
                         @RequestParam String direction,
                         @RequestParam(value = "redirect", defaultValue = "/") String redirect,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
    try {
      userPlaylists.moveItem(playlistId, itemId, direction, authentication);
      redirectAttributes.addFlashAttribute("message", "Playlist order updated.");
    } catch (ResponseStatusException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getReason() != null
          ? ex.getReason()
          : "Unable to update playlist order.");
    }
    return "redirect:" + safeRedirect(redirect);
  }

  @PostMapping("/actions/add-track")
  public String addTrack(@RequestParam Long trackId,
                         @RequestParam("targetPlaylistId") Long targetPlaylistId,
                         @RequestParam(value = "redirect", defaultValue = "/") String redirect,
                         Authentication authentication,
                         RedirectAttributes redirectAttributes) {
    try {
      userPlaylists.addTrackToPlaylist(targetPlaylistId, trackId, authentication);
      redirectAttributes.addFlashAttribute("message", "Track added to playlist.");
    } catch (ResponseStatusException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getReason() != null
          ? ex.getReason()
          : "Unable to add track to playlist.");
    }
    return "redirect:" + safeRedirect(redirect);
  }

  private String safeRedirect(String redirect) {
    if (redirect == null || redirect.isBlank() || redirect.startsWith("http")) {
      return "/";
    }
    return redirect.startsWith("/") ? redirect : "/" + redirect;
  }
}
