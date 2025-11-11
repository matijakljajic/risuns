package com.matijakljajic.music_catalog.service.profile;

import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class AccountPreferenceService {

  private final UserRepository users;

  @Transactional
  public void updateMessageNotifications(Long userId, boolean enabled, Authentication authentication) {
    User target = users.findById(userId)
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    User actor = requireUser(authentication);
    if (!canEdit(actor, target)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    target.setNotifyOnMessage(enabled);
  }

  private boolean canEdit(User actor, User target) {
    if (actor == null || target == null) {
      return false;
    }
    if (actor.isAdmin()) {
      return true;
    }
    return actor.getId() != null && actor.getId().equals(target.getId());
  }

  private User requireUser(Authentication authentication) {
    if (authentication == null
        || authentication instanceof AnonymousAuthenticationToken
        || !authentication.isAuthenticated()) {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
    }
    return users.findByUsername(authentication.getName())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
  }
}
