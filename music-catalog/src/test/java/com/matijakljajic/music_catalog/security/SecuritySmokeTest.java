// src/test/java/.../security/SecuritySmokeTest.java
package com.matijakljajic.music_catalog.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestBuilders.formLogin;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("h2")
class SecuritySmokeTest {

  @Autowired MockMvc mvc;

  @Test
  void publicEndpoints_areAccessibleWithoutLogin() throws Exception {
    mvc.perform(get("/")).andExpect(status().isOk());
    mvc.perform(get("/search")).andExpect(status().isOk());
    // static assets should also pass if present
    // mvc.perform(get("/css/app.css")).andExpect(status().isOk());
  }

  @Test
  void admin_requiresAdminRole() throws Exception {
    mvc.perform(get("/admin")).andExpect(status().is3xxRedirection()); // redirects to login
  }

  @Test
  void login_withDbUser_adminGetsIn() throws Exception {
    // Seed must have: username=admin, password={noop}admin, role=ADMIN
    mvc.perform(formLogin().user("admin").password("admin"))
       .andExpect(status().is3xxRedirection())
       .andExpect(redirectedUrl("/"));
    // After login, /admin should be 200 (assuming controller exists)
    // mvc.perform(get("/admin")).andExpect(status().isOk());
  }

  @Test
  void h2Console_isAdminOnly() throws Exception {
    mvc.perform(get("/h2-console")).andExpect(status().is3xxRedirection());
  }
}
