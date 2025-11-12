package com.matijakljajic.music_catalog.web.home;

import com.matijakljajic.music_catalog.service.catalog.GenreBrowseService;
import com.matijakljajic.music_catalog.service.listening.RecentListenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequiredArgsConstructor
public class HomeController {

  private final RecentListenService recentListens;
  private final GenreBrowseService genres;

  @GetMapping("/")
  public String home(Model model) {
    model.addAttribute("recentListens", recentListens.latest(10));
    model.addAttribute("genres", genres.allGenres());
    return "home/home";
  }

  @GetMapping("/ping")
  @ResponseBody
  public String ping() {
    return "ok";
  }
}
