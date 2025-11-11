package com.matijakljajic.music_catalog.web;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.matijakljajic.music_catalog.model.User;
import com.matijakljajic.music_catalog.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles({"h2", "seed-jpa"})
class UserReportControllerTest {

  @Autowired MockMvc mvc;
  @Autowired UserRepository users;

  private User owner;
  private User other;

  @BeforeEach
  void setUp() {
    owner = users.findByUsername("matija").orElseThrow();
    other = users.findByUsername("jelena3797").orElseThrow();
  }

  @Test
  void report_redirectsGuestsToLogin() throws Exception {
    mvc.perform(get("/users/" + owner.getId() + "/report"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrlPattern("**/login"));
  }

  @Test
  void report_forbidsOtherUsers() throws Exception {
    mvc.perform(get("/users/" + owner.getId() + "/report")
            .with(user(other.getUsername()).roles(other.getRole().name())))
        .andExpect(status().isForbidden());
  }

  @Test
  void report_viewIsServedToOwner() throws Exception {
    mvc.perform(get("/users/" + owner.getId() + "/report")
            .with(user(owner.getUsername()).roles(owner.getRole().name())))
        .andExpect(status().isOk());
  }

  @Test
  void reportDownload_returnsPdfForOwner() throws Exception {
    var result = mvc.perform(get("/users/" + owner.getId() + "/report/download")
            .with(user(owner.getUsername()).roles(owner.getRole().name())))
        .andExpect(status().isOk())
        .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION,
            org.hamcrest.Matchers.containsString("music-profile-" + owner.getId())))
        .andReturn();

    byte[] payload = result.getResponse().getContentAsByteArray();
    assertThat(payload).isNotNull();
    assertThat(payload.length).isGreaterThan(200);
    assertThat(result.getResponse().getContentType()).isEqualTo("application/pdf");
  }
}
