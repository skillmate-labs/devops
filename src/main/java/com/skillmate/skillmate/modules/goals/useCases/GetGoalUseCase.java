package com.skillmate.skillmate.modules.goals.useCases;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.skillmate.skillmate.modules.goals.GoalEntity;
import com.skillmate.skillmate.modules.goals.GoalRepository;
import com.skillmate.skillmate.modules.goals.dto.GoalDTO;
import com.skillmate.skillmate.modules.goals.mapper.GoalMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GetGoalUseCase {

  private final GoalRepository goalRepository;
  private final GoalMapper goalMapper;

  @Cacheable(value = "goals", key = "#id")
  public GoalDTO execute(String id) {
    GoalEntity goal = goalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Goal not found"));
    return goalMapper.toDTO(goal);
  }
}
