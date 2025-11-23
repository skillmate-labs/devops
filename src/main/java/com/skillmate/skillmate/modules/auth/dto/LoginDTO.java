package com.skillmate.skillmate.modules.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {

  @NotBlank(message = "{validation.login.email.notblank}")
  @Email(message = "{validation.login.email.invalid}")
  private String email;

  @NotBlank(message = "{validation.login.password.notblank}")
  private String password;
}
