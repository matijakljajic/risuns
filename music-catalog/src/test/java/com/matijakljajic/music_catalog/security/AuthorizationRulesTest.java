package com.matijakljajic.music_catalog.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class AuthorizationRulesTest {

  @Autowired MockMvc mvc;

  @Test
  void admin_requires_authentication() throws Exception {
    mvc.perform(get("/admin")).andExpect(status().is3xxRedirection());
  }

  @Test
  @WithMockUser(roles = "USER")
  void user_cannot_access_admin() throws Exception {
    mvc.perform(get("/admin")).andExpect(status().isForbidden());
  }

  @Test
  @WithMockUser(roles = "ADMIN")
  void admin_can_access_admin() throws Exception {
    mvc.perform(get("/admin")).andExpect(status().isOk());
  }
}
