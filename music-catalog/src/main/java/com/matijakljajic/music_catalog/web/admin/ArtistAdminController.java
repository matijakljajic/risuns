package com.matijakljajic.music_catalog.web.admin;

import com.matijakljajic.music_catalog.model.Artist;
import com.matijakljajic.music_catalog.service.admin.AdminArtistService;
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

  private final AdminArtistService artists;

  @GetMapping
  public String list(@RequestParam(value = "q", required = false) String query, Model model) {
    model.addAttribute("artists", artists.search(query));
    model.addAttribute("query", query);
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
    artists.create(artist);
    return "redirect:/admin/artists";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("artist", artists.get(id));
    return "admin/artists/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id, @Valid @ModelAttribute("artist") Artist artist, BindingResult br) {
    if (br.hasErrors()) return "admin/artists/form";
    artists.update(id, artist);
    return "redirect:/admin/artists";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    artists.delete(id);
    return "redirect:/admin/artists";
  }
}
