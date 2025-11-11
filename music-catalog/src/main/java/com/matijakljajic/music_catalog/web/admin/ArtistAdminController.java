package com.matijakljajic.music_catalog.web.admin;

import com.matijakljajic.music_catalog.model.Artist;
import com.matijakljajic.music_catalog.repository.ArtistRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/artists")
public class ArtistAdminController {

  private final ArtistRepository artists;

  @GetMapping
  public String list(Model model) {
    model.addAttribute("artists", artists.findAll());
    return "admin/artists/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("artist", new Artist());
    return "admin/artists/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("artist") Artist artist, BindingResult br) {
    if (br.hasErrors()) return "admin/artists/form";
    artists.save(artist);
    return "redirect:/admin/artists";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("artist", artists.findById(id).orElseThrow());
    return "admin/artists/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("artist") Artist artist, BindingResult br) {
    if (br.hasErrors()) return "admin/artists/form";
    artist.setId(id);
    artists.save(artist);
    return "redirect:/admin/artists";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    artists.deleteById(id);
    return "redirect:/admin/artists";
  }
}
