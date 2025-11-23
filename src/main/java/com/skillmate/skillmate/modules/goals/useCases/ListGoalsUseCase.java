package com.skillmate.skillmate.modules.goals.useCases;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.skillmate.skillmate.modules.goals.GoalEntity;
import com.skillmate.skillmate.modules.goals.GoalRepository;
import com.skillmate.skillmate.modules.goals.dto.GoalDTO;
import com.skillmate.skillmate.modules.goals.mapper.GoalMapper;
import com.skillmate.skillmate.modules.users.UserEntity;
import com.skillmate.skillmate.modules.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ListGoalsUseCase {

  private final GoalRepository goalRepository;
  private final UserRepository userRepository;
  private final GoalMapper goalMapper;

  @Cacheable(value = "goals", key = "#userId + '-' + #pageable.pageNumber + '-' + #pageable.pageSize")
  public Page<GoalDTO> execute(String userId, Pageable pageable) {
    if (userId != null) {
      UserEntity user = userRepository.findById(userId)
          .orElseThrow(() -> new RuntimeException("User not found"));
      Page<GoalEntity> goals = goalRepository.findByUser(user, pageable);
      return goals.map(goalMapper::toDTO);
    } else {
      Page<GoalEntity> goals = goalRepository.findAll(pageable);
      return goals.map(goalMapper::toDTO);
    }
  }
}
