package com.skillmate.skillmate.modules.database.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class GoalCompatibilityRequestDTO {
  @NotBlank(message = "User ID is required")
  private String userId;

  @NotBlank(message = "Goal ID is required")
  private String goalId;
}
