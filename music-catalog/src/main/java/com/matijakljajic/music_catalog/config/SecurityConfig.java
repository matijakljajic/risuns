package com.matijakljajic.music_catalog.config;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
        .requestMatchers(HttpMethod.GET, "/", "/search").permitAll()
        .requestMatchers("/signup", "/login").permitAll()
        .requestMatchers("/admin/**", "/h2-console/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .headers(h -> h.frameOptions(f -> f.sameOrigin()))
      .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
      // Use Spring’s default login until you implement /login
      .formLogin(fl -> fl
          //.loginPage("/login")
          .defaultSuccessUrl("/", true)
          .permitAll()
      )
      .logout(Customizer.withDefaults());

    return http.build();
  }
}
