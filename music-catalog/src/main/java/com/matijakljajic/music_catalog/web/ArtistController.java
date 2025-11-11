package com.matijakljajic.music_catalog.web;

import com.matijakljajic.music_catalog.service.catalog.ArtistViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/artists")
public class ArtistController {

  private final ArtistViewService artistViews;

  @GetMapping("/{id}")
  public String view(@PathVariable Long id, Model model) {
    var view = artistViews.getArtist(id);
    model.addAttribute("artist", view.getArtist());
    model.addAttribute("albums", view.getAlbums());
    return "artist/view";
  }
}
