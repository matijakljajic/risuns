package com.matijakljajic.music_catalog.web;

import com.matijakljajic.music_catalog.service.catalog.PlaylistViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Controller
@RequiredArgsConstructor
@RequestMapping("/playlists")
public class PlaylistController {

  private static final DateTimeFormatter HUMAN_DATETIME =
      DateTimeFormatter.ofPattern("d MMM uuuu · HH:mm").withZone(ZoneId.systemDefault());

  private final PlaylistViewService playlistViews;

  @GetMapping("/{id}")
  public String view(@PathVariable Long id, Authentication authentication, Model model) {
    var view = playlistViews.getPlaylist(id, authentication);
    model.addAttribute("playlist", view.getPlaylist());
    model.addAttribute("items", view.getItems());
    model.addAttribute("playlistTopListeners", view.getTopListeners());
    if (view.getPlaylist().getCreatedAt() != null) {
      model.addAttribute("playlistCreatedAt",
          HUMAN_DATETIME.format(view.getPlaylist().getCreatedAt()));
    }
    return "playlist/view";
  }
}
