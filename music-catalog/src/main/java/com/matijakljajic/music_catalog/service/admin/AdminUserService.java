package com.matijakljajic.music_catalog.service.admin;

import com.matijakljajic.music_catalog.model.Role;
import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AdminUserService {

  private final UserRepository users;
  private final PasswordEncoder passwordEncoder;

  public List<User> findAll() {
    return users.findAll();
  }

  public User get(Long id) {
    return users.findById(id).orElseThrow();
  }

  public Role[] roles() {
    return Role.values();
  }

  @Transactional
  public User create(User user, String rawPassword) {
    if (rawPassword == null || rawPassword.isBlank()) {
      throw new IllegalArgumentException("Password is required");
    }
    user.setId(null);
    user.setPasswordHash(passwordEncoder.encode(rawPassword));
    return users.save(user);
  }

  @Transactional
  public User update(Long id, User form, String rawPassword) {
    User managed = users.findById(id).orElseThrow();
    managed.setUsername(form.getUsername());
    managed.setEmail(form.getEmail());
    managed.setDisplayName(form.getDisplayName());
    managed.setRole(form.getRole());
    managed.setEnabled(form.isEnabled());
    if (rawPassword != null && !rawPassword.isBlank()) {
      managed.setPasswordHash(passwordEncoder.encode(rawPassword));
    }
    return managed;
  }

  @Transactional
  public void delete(Long id) {
    users.deleteById(id);
  }
}
