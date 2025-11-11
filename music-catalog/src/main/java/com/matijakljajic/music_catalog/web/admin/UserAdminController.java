package com.matijakljajic.music_catalog.web.admin;

import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.service.admin.AdminUserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/users")
public class UserAdminController {

  private final AdminUserService users;

  @GetMapping
  public String list(Model model) {
    model.addAttribute("users", users.findAll());
    return "admin/users/list";
  }

  @GetMapping("/new")
  public String createForm(Model model) {
    model.addAttribute("user", new User());
    model.addAttribute("roles", users.roles());
    return "admin/users/form";
  }

  @PostMapping
  public String create(@Valid @ModelAttribute("user") User user,
                       BindingResult br,
                       @RequestParam("rawPassword") String rawPassword,
                       Model model) {
    if (br.hasErrors()) {
      model.addAttribute("roles", users.roles());
      return "admin/users/form";
    }
    if (rawPassword == null || rawPassword.isBlank()) {
      br.rejectValue("passwordHash", "password.required", "Password is required");
      model.addAttribute("roles", users.roles());
      return "admin/users/form";
    }
    users.create(user, rawPassword);
    return "redirect:/admin/users";
  }

  @GetMapping("/{id}/edit")
  public String editForm(@PathVariable Long id, Model model) {
    model.addAttribute("user", users.get(id));
    model.addAttribute("roles", users.roles());
    return "admin/users/form";
  }

  @PostMapping("/{id}")
  public String update(@PathVariable Long id,
                       @Valid @ModelAttribute("user") User user,
                       BindingResult br,
                       @RequestParam(value = "rawPassword", required = false) String rawPassword,
                       Model model) {
    if (br.hasErrors()) {
      model.addAttribute("roles", users.roles());
      return "admin/users/form";
    }
    users.update(id, user, rawPassword);
    return "redirect:/admin/users";
  }

  @PostMapping("/{id}/delete")
  public String delete(@PathVariable Long id) {
    users.delete(id);
    return "redirect:/admin/users";
  }
}
