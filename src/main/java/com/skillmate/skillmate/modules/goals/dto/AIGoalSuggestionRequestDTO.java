package com.skillmate.skillmate.modules.goals.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIGoalSuggestionRequestDTO {
  @NotBlank(message = "{validation.ai-suggestion.experience.notblank}")
  private String experience;

  @NotBlank(message = "{validation.ai-suggestion.skill.notblank}")
  private String skill;
}
