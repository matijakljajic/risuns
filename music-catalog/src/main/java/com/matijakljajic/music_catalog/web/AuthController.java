// src/main/java/com/matijakljajic/music_catalog/web/AuthController.java
package com.matijakljajic.music_catalog.web;

import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.UserRepository;
import com.matijakljajic.music_catalog.web.auth.SignupForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  @GetMapping("/login")
  public String login(@RequestParam(value = "error", required = false) String error,
                      @RequestParam(value = "logout", required = false) String logout,
                      @RequestParam(value = "registered", required = false) String registered,
                      Model model) {
    model.addAttribute("loginError", error != null);
    model.addAttribute("logoutMessage", logout != null);
    boolean signupSuccess = registered != null || model.containsAttribute("registeredUsername");
    model.addAttribute("signupSuccess", signupSuccess);
    if (!model.containsAttribute("registeredUsername")) {
      model.addAttribute("registeredUsername", null);
    }
    return "auth/login";
  }

  @GetMapping("/signup")
  public String signup(Model model) {
    if (!model.containsAttribute("signupForm")) {
      model.addAttribute("signupForm", new SignupForm());
    }
    return "auth/signup";
  }

  @PostMapping("/signup")
  public String handleSignup(@Valid @ModelAttribute("signupForm") SignupForm signupForm,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
    String username = trim(signupForm.getUsername());
    String email = trim(signupForm.getEmail());
    String displayName = trimToNull(signupForm.getDisplayName());
    signupForm.setUsername(username);
    signupForm.setEmail(email);
    signupForm.setDisplayName(displayName);

    if (!signupForm.passwordsMatch()) {
      bindingResult.rejectValue("confirmPassword", "signup.passwordsMismatch", "Passwords do not match.");
    }

    if (!bindingResult.hasErrors() && username != null && userRepository.existsByUsername(username)) {
      bindingResult.rejectValue("username", "signup.username.taken", "Username is already in use.");
    }

    if (!bindingResult.hasErrors() && email != null && userRepository.existsByEmail(email)) {
      bindingResult.rejectValue("email", "signup.email.taken", "That email address already has an account.");
    }

    if (bindingResult.hasErrors()) {
      return "auth/signup";
    }

    User user = new User();
    user.setUsername(username);
    user.setEmail(email);
    user.setDisplayName(displayName);
    user.setPasswordHash(passwordEncoder.encode(signupForm.getPassword()));
    user.setNotifyOnMessage(signupForm.isNotifyOnMessage());
    userRepository.save(user);

    redirectAttributes.addFlashAttribute("registeredUsername", username);
    return "redirect:/login?registered";
  }

  private static String trim(String value) {
    return value == null ? null : value.trim();
  }

  private static String trimToNull(String value) {
    if (value == null) {
      return null;
    }
    String trimmed = value.trim();
    return trimmed.isEmpty() ? null : trimmed;
  }
}
