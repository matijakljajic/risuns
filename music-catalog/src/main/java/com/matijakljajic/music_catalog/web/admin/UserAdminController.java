package com.matijakljajic.music_catalog.web.admin;

import com.matijakljajic.music_catalog.model.Role;
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
  public String list(@RequestParam(value = "q", required = false) String query,
                     @RequestParam(value = "role", required = false) String roleParam,
                     @RequestParam(value = "status", required = false) String statusParam,
                     Model model) {
    Role roleFilter = resolveRole(roleParam);
    Boolean enabled = resolveStatus(statusParam);
    model.addAttribute("users", users.search(query, roleFilter, enabled));
    model.addAttribute("query", query);
    model.addAttribute("roleFilter", roleParam);
    model.addAttribute("statusFilter", statusParam);
    model.addAttribute("roles", users.roles());
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

  private Role resolveRole(String name) {
    if (name == null || name.isBlank()) {
      return null;
    }
    try {
      return Role.valueOf(name);
    } catch (IllegalArgumentException ex) {
      return null;
    }
  }

  private Boolean resolveStatus(String status) {
    if (status == null || status.isBlank() || "all".equalsIgnoreCase(status)) {
      return null;
    }
    if ("enabled".equalsIgnoreCase(status) || "true".equalsIgnoreCase(status)) {
      return Boolean.TRUE;
    }
    if ("disabled".equalsIgnoreCase(status) || "false".equalsIgnoreCase(status)) {
      return Boolean.FALSE;
    }
    return null;
  }
}
