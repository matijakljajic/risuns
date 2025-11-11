package com.matijakljajic.music_catalog.web;

import com.matijakljajic.music_catalog.service.library.UserPlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.server.ResponseStatusException;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/playlists")
public class UserPlaylistController {

  private final UserPlaylistService userPlaylists;

  @GetMapping("/manage")
  public String manage(@PathVariable Long userId,
                       Authentication authentication,
                       Model model) {
    var view = userPlaylists.buildManageView(userId, authentication);
    model.addAttribute("owner", view.getOwner());
    model.addAttribute("playlists", view.getPlaylists());
    model.addAttribute("self", view.isSelf());
    return "user/manage-playlists";
  }

  @PostMapping
  public String create(@PathVariable Long userId,
                       @RequestParam String name,
                       @RequestParam(name = "public", defaultValue = "false") boolean isPublic,
                       Authentication authentication,
                       RedirectAttributes redirectAttributes) {
    try {
      userPlaylists.createPlaylist(userId, name, isPublic, authentication);
      redirectAttributes.addFlashAttribute("message", "Playlist created.");
    } catch (ResponseStatusException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getReason() != null
          ? ex.getReason()
          : "Unable to create playlist.");
    }
    return "redirect:/users/" + userId + "/playlists/manage";
  }

  @PostMapping("/{playlistId}/update")
  public String update(@PathVariable Long userId,
                       @PathVariable Long playlistId,
                       @RequestParam String name,
                       @RequestParam(name = "public", defaultValue = "false") boolean isPublic,
                       Authentication authentication,
                       RedirectAttributes redirectAttributes) {
    try {
      userPlaylists.updatePlaylist(userId, playlistId, name, isPublic, authentication);
      redirectAttributes.addFlashAttribute("message", "Playlist updated.");
    } catch (ResponseStatusException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getReason() != null
          ? ex.getReason()
          : "Unable to update playlist.");
    }
    return "redirect:/users/" + userId + "/playlists/manage";
  }

  @PostMapping("/{playlistId}/delete")
  public String delete(@PathVariable Long userId,
                       @PathVariable Long playlistId,
                       Authentication authentication,
                       RedirectAttributes redirectAttributes) {
    try {
      userPlaylists.deletePlaylist(userId, playlistId, authentication);
      redirectAttributes.addFlashAttribute("message", "Playlist deleted.");
    } catch (ResponseStatusException ex) {
      redirectAttributes.addFlashAttribute("message", ex.getReason() != null
          ? ex.getReason()
          : "Unable to delete playlist.");
    }
    return "redirect:/users/" + userId + "/playlists/manage";
  }
}
