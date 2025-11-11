package com.matijakljajic.music_catalog.web.admin;

import com.matijakljajic.music_catalog.model.Genre;
import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.repository.AlbumRepository;
import com.matijakljajic.music_catalog.repository.GenreRepository;
import com.matijakljajic.music_catalog.repository.TrackRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/tracks")
public class TrackAdminController {

  private final TrackRepository tracks;
  private final AlbumRepository albums;
  private final GenreRepository genres;

  @GetMapping
  public String list(Model model) {
    model.addAttribute("tracks", tracks.findAll());
    return "admin/tracks/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("track", new Track());
    model.addAttribute("albums", albums.findAll());
    model.addAttribute("genres", genres.findAll());
    return "admin/tracks/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("track") Track track,
                       BindingResult br,
                       Model model,
                       @RequestParam(value="genreIds", required=false) Long[] genreIds) {
    if (br.hasErrors()) {
      model.addAttribute("albums", albums.findAll());
      model.addAttribute("genres", genres.findAll());
      return "admin/tracks/form";
    }
    track.getGenres().clear();
    if (genreIds != null) {
      for (Long gid : genreIds) {
        Genre g = genres.findById(gid).orElseThrow();
        track.getGenres().add(g);
      }
    }
    tracks.save(track);
    return "redirect:/admin/tracks";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    Track t = tracks.findById(id).orElseThrow();
    model.addAttribute("track", t);
    model.addAttribute("albums", albums.findAll());
    model.addAttribute("genres", genres.findAll());
    return "admin/tracks/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
                       @Valid @ModelAttribute("track") Track track,
                       BindingResult br,
                       Model model,
                       @RequestParam(value="genreIds", required=false) Long[] genreIds) {
    if (br.hasErrors()) {
      model.addAttribute("albums", albums.findAll());
      model.addAttribute("genres", genres.findAll());
      return "admin/tracks/form";
    }
    track.setId(id);
    track.getGenres().clear();
    if (genreIds != null) {
      for (Long gid : genreIds) {
        track.getGenres().add(genres.findById(gid).orElseThrow());
      }
    }
    tracks.save(track);
    return "redirect:/admin/tracks";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    tracks.deleteById(id);
    return "redirect:/admin/tracks";
  }
}
