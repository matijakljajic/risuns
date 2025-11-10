package com.matijakljajic.music_catalog;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {

  @GetMapping("/admin")
  public String admin(Model model, Authentication authentication) {
    model.addAttribute("username", authentication != null ? authentication.getName() : "admin");
    return "admin";
  }
}
