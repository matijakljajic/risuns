package com.matijakljajic.music_catalog.web;

import com.matijakljajic.music_catalog.service.catalog.AlbumViewService;
import com.matijakljajic.music_catalog.service.library.UserPlaylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/albums")
public class AlbumController {

  private final AlbumViewService albumViews;
  private final UserPlaylistService userPlaylists;

  @GetMapping("/{id}")
  public String view(@PathVariable Long id, Authentication authentication, Model model) {
    var view = albumViews.getAlbum(id);
    model.addAttribute("album", view.getAlbum());
    model.addAttribute("tracks", view.getTracks());
    model.addAttribute("albumTopListeners", view.getTopListeners());
    model.addAttribute("myPlaylists", userPlaylists.ownedPlaylists(authentication));
    return "album/view";
  }
}
