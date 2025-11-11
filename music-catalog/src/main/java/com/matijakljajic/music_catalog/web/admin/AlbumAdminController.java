package com.matijakljajic.music_catalog.web.admin;

import com.matijakljajic.music_catalog.model.Album;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.ArtistRepository;
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

  private final AlbumRepository albums;
  private final ArtistRepository artists;

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
    albums.save(album);
    return "redirect:/admin/albums";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("album", albums.findById(id).orElseThrow());
    model.addAttribute("artists", artists.findAll());
    return "admin/albums/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("album") Album album, BindingResult br, Model model) {
    if (br.hasErrors()) {
      model.addAttribute("artists", artists.findAll());
      return "admin/albums/form";
    }
    album.setId(id);
    albums.save(album);
    return "redirect:/admin/albums";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    albums.deleteById(id);
    return "redirect:/admin/albums";
  }
}
