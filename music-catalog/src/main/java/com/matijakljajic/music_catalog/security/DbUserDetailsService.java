package com.matijakljajic.music_catalog.security;

import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.UserRepository;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DbUserDetailsService implements UserDetailsService {
  private final UserRepository users;

  public DbUserDetailsService(UserRepository users) {
    this.users = users;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User u = users.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Not found: " + username));

    if (!u.isEnabled()) {
      throw new DisabledException("Account disabled");
    }

    var auths = List.of(new SimpleGrantedAuthority("ROLE_" + u.getRole().name()));
    return new org.springframework.security.core.userdetails.User(
        u.getUsername(),
        u.getPasswordHash(),
        u.isEnabled(),
        true, true, true,
        auths
    );
  }
}
