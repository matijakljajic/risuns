package com.matijakljajic.music_catalog.web.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupForm {

  @NotBlank
  @Size(min = 3, max = 40)
  private String username;

  @NotBlank
  @Email
  @Size(max = 255)
  private String email;

  @Size(max = 255)
  private String displayName;

  @NotBlank
  @Size(min = 6, max = 72)
  private String password;

  @NotBlank
  private String confirmPassword;

  private boolean notifyOnMessage;

  public boolean passwordsMatch() {
    return password != null && password.equals(confirmPassword);
  }
}
