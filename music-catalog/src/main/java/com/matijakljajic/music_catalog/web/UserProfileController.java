package com.matijakljajic.music_catalog.web;

import com.matijakljajic.music_catalog.service.profile.UserProfileService;
import com.matijakljajic.music_catalog.service.profile.UserProfileService.ProfileView;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserProfileController {

  private final UserProfileService userProfiles;

  @GetMapping("/{id}")
  public String view(@PathVariable Long id,
                     @RequestParam(value = "page", defaultValue = "0") int page,
                     Authentication authentication,
                     Model model) {
    ProfileView view = userProfiles.buildProfile(id, authentication, page);
    model.addAttribute("profile", view.getProfile());
    model.addAttribute("visiblePlaylists", view.getVisiblePlaylists());
    model.addAttribute("isSelf", view.isSelf());
    model.addAttribute("canMessage", !view.isSelf());
    model.addAttribute("publicPlaylistCount", view.getPublicPlaylistCount());
    model.addAttribute("privatePlaylistCount", view.getPrivatePlaylistCount());
    model.addAttribute("weeklyListensPage", view.getWeeklyListensPage());
    model.addAttribute("weeklyListenCount", view.getWeeklyListenCount());
    model.addAttribute("weeklyUniqueTracks", view.getWeeklyUniqueTracks());
    model.addAttribute("weeklySince", view.getWeeklySince());
    model.addAttribute("page", view.getPage());
    model.addAttribute("hasNext", view.isHasNext());
    model.addAttribute("hasPrev", view.isHasPrev());
    model.addAttribute("pageStart", view.getPageStart());
    model.addAttribute("pageEnd", view.getPageEnd());
    model.addAttribute("topTracks", view.getTopTracks());
    return "user/view";
  }
}
