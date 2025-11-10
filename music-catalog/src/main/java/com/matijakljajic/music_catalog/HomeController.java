package com.matijakljajic.music_catalog;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HomeController {
  @GetMapping("/") public String home() { return "home"; }

  // --- Diagnostic endpoint ---
  @GetMapping("/ping")
  @ResponseBody
  public String ping() {
    return "ok";
  }
}

