package com.matijakljajic.music_catalog.web;

import com.matijakljajic.music_catalog.service.profile.AccountPreferenceService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/preferences")
public class UserPreferencesController {

  private final AccountPreferenceService preferences;

  @PostMapping("/message-notifications")
  public String updateMessageNotifications(@PathVariable Long userId,
                                           @RequestParam(name = "notifyOnMessage", defaultValue = "false")
                                           boolean notifyOnMessage,
                                           Authentication authentication,
                                           RedirectAttributes redirectAttributes) {
    preferences.updateMessageNotifications(userId, notifyOnMessage, authentication);
    redirectAttributes.addFlashAttribute("message", "Notification preference updated.");
    return "redirect:/users/" + userId;
  }
}
