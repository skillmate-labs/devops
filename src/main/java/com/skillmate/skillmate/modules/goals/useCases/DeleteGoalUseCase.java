package com.skillmate.skillmate.modules.goals.useCases;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillmate.skillmate.modules.goals.GoalRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteGoalUseCase {

  private final GoalRepository goalRepository;

  @Transactional
  @CacheEvict(value = { "goals" }, allEntries = true, key = "#id")
  public void execute(String id) {
    if (!goalRepository.existsById(id)) {
      throw new RuntimeException("Goal not found");
    }
    goalRepository.deleteById(id);
  }
}
