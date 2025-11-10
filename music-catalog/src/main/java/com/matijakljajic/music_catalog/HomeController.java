package com.matijakljajic.music_catalog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
  @GetMapping("/")
  public String home() { return "home"; }

  @GetMapping("/search")
  public String search() { return "search"; }

  // --- Diagnostic endpoint ---
  @GetMapping("/ping")
  @ResponseBody
  public String ping() {
    return "ok";
  }
}
