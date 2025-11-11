package com.matijakljajic.music_catalog.web.admin;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.service.admin.AdminAlbumService;
import com.matijakljajic.music_catalog.service.admin.AdminArtistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/albums")
public class AlbumAdminController {

  private final AdminAlbumService albums;
  private final AdminArtistService artists;

  @GetMapping
  public String list(Model model) {
    model.addAttribute("albums", albums.findAll());
    return "admin/albums/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("album", new Album());
    model.addAttribute("artists", artists.findAll());
    return "admin/albums/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("album") Album album, BindingResult br, Model model) {
    if (br.hasErrors()) {
      model.addAttribute("artists", artists.findAll());
      return "admin/albums/form";
    }
    albums.create(album);
    return "redirect:/admin/albums";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("album", albums.get(id));
    model.addAttribute("artists", artists.findAll());
    return "admin/albums/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("album") Album album, BindingResult br, Model model) {
    if (br.hasErrors()) {
      model.addAttribute("artists", artists.findAll());
      return "admin/albums/form";
    }
    albums.update(id, album);
    return "redirect:/admin/albums";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    albums.delete(id);
    return "redirect:/admin/albums";
  }
}
