package com.skillmate.skillmate.modules.roles.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

  private String id;

  @NotBlank(message = "{validation.role.name.notblank}")
  @Size(max = 50, message = "{validation.role.name.size}")
  private String name;

  @NotBlank(message = "{validation.role.acronym.notblank}")
  @Size(max = 10, message = "{validation.role.acronym.size}")
  private String acronym;

  private LocalDateTime createdAt;
}
