package com.matijakljajic.music_catalog.web.admin;

import com.matijakljajic.music_catalog.model.Genre;
import com.matijakljajic.music_catalog.repository.GenreRepository;
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

  private final GenreRepository genres;

  @GetMapping
  public String list(Model model) {
    model.addAttribute("genres", genres.findAll());
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
    genres.save(genre);
    return "redirect:/admin/genres";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("genre", genres.findById(id).orElseThrow());
    return "admin/genres/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
                       @Valid @ModelAttribute("genre") Genre genre, BindingResult br) {
    if (br.hasErrors()) return "admin/genres/form";
    genre.setId(id);
    genres.save(genre);
    return "redirect:/admin/genres";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    genres.deleteById(id);
    return "redirect:/admin/genres";
  }
}
