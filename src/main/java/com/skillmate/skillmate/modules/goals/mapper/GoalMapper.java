package com.skillmate.skillmate.modules.goals.mapper;

import org.springframework.stereotype.Component;

import com.skillmate.skillmate.modules.goals.GoalEntity;
import com.skillmate.skillmate.modules.goals.dto.GoalDTO;

@Component
public class GoalMapper {

  public GoalDTO toDTO(GoalEntity entity) {
    if (entity == null) {
      return null;
    }
    GoalDTO dto = new GoalDTO();
    dto.setId(entity.getId());
    dto.setTitle(entity.getTitle());
    dto.setExperience(entity.getExperience());
    dto.setHoursPerDay(entity.getHoursPerDay());
    dto.setDaysPerWeek(entity.getDaysPerWeek());
    dto.setUserId(entity.getUser() != null ? entity.getUser().getId() : null);
    dto.setUserName(entity.getUser() != null ? entity.getUser().getName() : null);
    dto.setCreatedById(entity.getCreatedBy() != null ? entity.getCreatedBy().getId() : null);
    dto.setCreatedByName(entity.getCreatedBy() != null ? entity.getCreatedBy().getName() : null);
    dto.setUpdatedById(entity.getUpdatedBy() != null ? entity.getUpdatedBy().getId() : null);
    dto.setUpdatedByName(entity.getUpdatedBy() != null ? entity.getUpdatedBy().getName() : null);
    dto.setCreatedAt(entity.getCreatedAt());
    dto.setUpdatedAt(entity.getUpdatedAt());
    return dto;
  }

  public GoalEntity toEntity(GoalDTO dto) {
    if (dto == null) {
      return null;
    }
    GoalEntity entity = new GoalEntity();
    entity.setId(dto.getId());
    entity.setTitle(dto.getTitle());
    entity.setExperience(dto.getExperience());
    entity.setHoursPerDay(dto.getHoursPerDay());
    entity.setDaysPerWeek(dto.getDaysPerWeek());
    return entity;
  }
}
