package com.matijakljajic.music_catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@Profile("h2") // only active for the H2 profile
public class SecurityConfigH2 {

  @Bean
  SecurityFilterChain h2FilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        // H2 console REQUIRES AUTH:
        .requestMatchers("/h2-console/**").authenticated()
        // everything else also requires auth (adjust if you have public pages)
        .anyRequest().authenticated()
      )
      // H2 console posts without CSRF token → ignore CSRF for that path only
      .csrf(csrf -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/h2-console/**")))
      // H2 UI is rendered in a frame → allow same-origin frames
      .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin()))
      .formLogin(Customizer.withDefaults())
      .logout(Customizer.withDefaults());

    return http.build();
  }
}
