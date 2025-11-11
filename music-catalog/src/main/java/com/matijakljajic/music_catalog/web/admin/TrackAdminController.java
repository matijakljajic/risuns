package com.matijakljajic.music_catalog.web.admin;

import com.matijakljajic.music_catalog.model.Track;
import com.matijakljajic.music_catalog.service.admin.AdminTrackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/tracks")
public class TrackAdminController {

  private final AdminTrackService tracks;

  @GetMapping
  public String list(@RequestParam(value = "q", required = false) String query,
                     @RequestParam(value = "genreId", required = false) Long genreId,
                     @RequestParam(value = "explicit", required = false) String explicitParam,
                     Model model) {
    Boolean explicit = resolveExplicitFilter(explicitParam);
    model.addAttribute("tracks", tracks.search(query, genreId, explicit));
    model.addAttribute("query", query);
    model.addAttribute("genreId", genreId);
    model.addAttribute("explicitFilter", explicitParam);
    model.addAttribute("genres", tracks.allGenres());
    return "admin/tracks/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("track", new Track());
    populateLookups(model);
    return "admin/tracks/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("track") Track track,
                       BindingResult br,
                       Model model,
                       @RequestParam(value="genreIds", required=false) Long[] genreIds,
                       @RequestParam(value="featureArtistIds", required=false) Long[] featureArtistIds) {
    if (br.hasErrors()) {
      populateLookups(model);
      return "admin/tracks/form";
    }
    tracks.create(track, toIdList(genreIds), toIdList(featureArtistIds));
    return "redirect:/admin/tracks";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("track", tracks.get(id));
    populateLookups(model);
    return "admin/tracks/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
                       @Valid @ModelAttribute("track") Track track,
                       BindingResult br,
                       Model model,
                       @RequestParam(value="genreIds", required=false) Long[] genreIds,
                       @RequestParam(value="featureArtistIds", required=false) Long[] featureArtistIds) {
    if (br.hasErrors()) {
      populateLookups(model);
      return "admin/tracks/form";
    }
    tracks.update(id, track, toIdList(genreIds), toIdList(featureArtistIds));
    return "redirect:/admin/tracks";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    tracks.delete(id);
    return "redirect:/admin/tracks";
  }

  private void populateLookups(Model model) {
    model.addAttribute("albums", tracks.allAlbums());
    model.addAttribute("genres", tracks.allGenres());
    model.addAttribute("artists", tracks.allArtists());
  }

  private List<Long> toIdList(Long[] ids) {
    return (ids == null || ids.length == 0) ? List.of() : Arrays.asList(ids);
  }

  private Boolean resolveExplicitFilter(String value) {
    if (value == null || value.isBlank() || "all".equalsIgnoreCase(value)) {
      return null;
    }
    return ("explicit".equalsIgnoreCase(value) || "true".equalsIgnoreCase(value)) ? Boolean.TRUE
        : ("clean".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) ? Boolean.FALSE
        : null;
  }
}
