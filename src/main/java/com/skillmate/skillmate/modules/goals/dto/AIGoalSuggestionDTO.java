package com.skillmate.skillmate.modules.goals.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIGoalSuggestionDTO {
  private String title;
  private String experience;
  private Integer suggestedHoursPerDay;
  private Integer suggestedDaysPerWeek;
  private String reasoning;
}
