// src/main/java/com/matijakljajic/music_catalog/config/SecurityConfig.java
package com.matijakljajic.music_catalog.config;

import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

  @Bean
  SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests(auth -> auth
        .dispatcherTypeMatchers(DispatcherType.FORWARD, DispatcherType.ERROR).permitAll()
        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
        .requestMatchers("/webjars/**", "/assets/**", "/static/**", "/css/**", "/js/**", "/images/**").permitAll()
        .requestMatchers("/users/*/report/**").authenticated()
        .requestMatchers(HttpMethod.GET,
            "/", "/search", "/search/**", "/artists/**", "/albums/**", "/playlists/**", "/users/**", "/ping"
        ).permitAll()
        .requestMatchers("/login", "/signup", "/register", "/error").permitAll()
        .requestMatchers("/h2-console/**").hasRole("ADMIN")
        .requestMatchers("/admin/**").hasRole("ADMIN")
        .anyRequest().authenticated()
      )
      .headers(h -> h.frameOptions(f -> f.sameOrigin()))
      .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
      .formLogin(fl -> fl
        .loginPage("/login")
        .loginProcessingUrl("/login")
        .defaultSuccessUrl("/", true)
        .failureUrl("/login?error")
        .permitAll()
      )
      .logout(logout -> logout
        .logoutUrl("/logout")
        .logoutSuccessUrl("/login?logout")
        .permitAll()
      );

    return http.build();
  }

}
