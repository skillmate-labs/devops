package com.skillmate.skillmate.modules.users.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

  private String id;

  @NotBlank(message = "{validation.user.name.notblank}")
  @Size(min = 3, max = 150, message = "{validation.user.name.size}")
  private String name;

  @NotBlank(message = "{validation.user.email.notblank}")
  @Email(message = "{validation.user.email.invalid}")
  @Size(max = 150, message = "{validation.user.email.size}")
  private String email;

  @NotBlank(message = "{validation.user.password.notblank}")
  @Size(min = 6, max = 100, message = "{validation.user.password.size}")
  private String password;

  @NotBlank(message = "{validation.user.roleId.notblank}")
  private String roleId;

  private String roleName;
}
