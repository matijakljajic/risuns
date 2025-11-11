package com.matijakljajic.music_catalog.web;

import com.matijakljajic.music_catalog.service.catalog.AlbumViewService;
import lombok.RequiredArgsConstructor;
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

  @GetMapping("/{id}")
  public String view(@PathVariable Long id, Model model) {
    var view = albumViews.getAlbum(id);
    model.addAttribute("album", view.getAlbum());
    model.addAttribute("tracks", view.getTracks());
    model.addAttribute("albumTopListeners", view.getTopListeners());
    return "album/view";
  }
}
