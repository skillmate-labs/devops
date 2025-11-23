package com.skillmate.skillmate.modules.goals.useCases;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillmate.skillmate.config.MessageProducer;
import com.skillmate.skillmate.modules.goals.GoalEntity;
import com.skillmate.skillmate.modules.goals.GoalRepository;
import com.skillmate.skillmate.modules.goals.dto.GoalDTO;
import com.skillmate.skillmate.modules.goals.mapper.GoalMapper;
import com.skillmate.skillmate.modules.users.UserEntity;
import com.skillmate.skillmate.modules.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UpdateGoalUseCase {

  private final GoalRepository goalRepository;
  private final UserRepository userRepository;
  private final GoalMapper goalMapper;
  private final MessageProducer messageProducer;

  @Transactional
  @CacheEvict(value = { "goals" }, allEntries = true)
  @CachePut(value = "goals", key = "#id")
  public GoalDTO execute(String id, GoalDTO goalDTO, String currentUserId) {
    GoalEntity goal = goalRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Goal not found"));

    UserEntity updatedBy = userRepository.findById(currentUserId)
        .orElseThrow(() -> new RuntimeException("Current user not found"));

    goal.setTitle(goalDTO.getTitle());
    goal.setExperience(goalDTO.getExperience());
    goal.setHoursPerDay(goalDTO.getHoursPerDay());
    goal.setDaysPerWeek(goalDTO.getDaysPerWeek());
    goal.setUpdatedBy(updatedBy);

    GoalEntity savedGoal = goalRepository.save(goal);
    GoalDTO updatedGoalDTO = goalMapper.toDTO(savedGoal);
    messageProducer.sendGoalUpdated(updatedGoalDTO);
    return updatedGoalDTO;
  }
}
