package com.matijakljajic.music_catalog.web.admin;

import com.matijakljajic.music_catalog.model.Genre;
import com.matijakljajic.music_catalog.service.admin.AdminGenreService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/genres")
public class GenreAdminController {

  private final AdminGenreService genres;

  @GetMapping
  public String list(@RequestParam(value = "q", required = false) String query, Model model) {
    model.addAttribute("genres", genres.search(query));
    model.addAttribute("query", query);
    return "admin/genres/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("genre", new Genre());
    return "admin/genres/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("genre") Genre genre, BindingResult br) {
    if (br.hasErrors()) return "admin/genres/form";
    genres.create(genre);
    return "redirect:/admin/genres";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("genre", genres.get(id));
    return "admin/genres/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
                       @Valid @ModelAttribute("genre") Genre genre, BindingResult br) {
    if (br.hasErrors()) return "admin/genres/form";
    genres.update(id, genre);
    return "redirect:/admin/genres";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    genres.delete(id);
    return "redirect:/admin/genres";
  }
}
