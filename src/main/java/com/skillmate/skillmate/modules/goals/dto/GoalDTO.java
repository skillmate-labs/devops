package com.skillmate.skillmate.modules.goals.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GoalDTO {

  private String id;

  @NotBlank(message = "{validation.goal.title.notblank}")
  @Size(max = 500, message = "{validation.goal.title.size}")
  private String title;

  @NotBlank(message = "{validation.goal.experience.notblank}")
  @Size(max = 2000, message = "{validation.goal.experience.size}")
  private String experience;

  @NotNull(message = "{validation.goal.hoursPerDay.notnull}")
  @Min(value = 1, message = "{validation.goal.hoursPerDay.min}")
  @Max(value = 24, message = "{validation.goal.hoursPerDay.max}")
  private Integer hoursPerDay;

  @NotNull(message = "{validation.goal.daysPerWeek.notnull}")
  @Min(value = 1, message = "{validation.goal.daysPerWeek.min}")
  @Max(value = 7, message = "{validation.goal.daysPerWeek.max}")
  private Integer daysPerWeek;

  private String userId;
  private String userName;
  private String createdById;
  private String createdByName;
  private String updatedById;
  private String updatedByName;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
