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
public class CreateGoalUseCase {

  private final GoalRepository goalRepository;
  private final UserRepository userRepository;
  private final GoalMapper goalMapper;
  private final MessageProducer messageProducer;

  @Transactional
  @CacheEvict(value = { "goals" }, allEntries = true)
  @CachePut(value = "goals", key = "#result.id")
  public GoalDTO execute(GoalDTO goalDTO, String currentUserId) {
    UserEntity user = userRepository.findById(goalDTO.getUserId())
        .orElseThrow(() -> new RuntimeException("User not found"));

    UserEntity createdBy = userRepository.findById(currentUserId)
        .orElseThrow(() -> new RuntimeException("Current user not found"));

    GoalEntity goal = goalMapper.toEntity(goalDTO);
    goal.setUser(user);
    goal.setCreatedBy(createdBy);

    GoalEntity savedGoal = goalRepository.save(goal);
    GoalDTO savedGoalDTO = goalMapper.toDTO(savedGoal);
    messageProducer.sendGoalCreated(savedGoalDTO);
    return savedGoalDTO;
  }
}
