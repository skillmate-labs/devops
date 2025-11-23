package com.skillmate.skillmate.modules.users.useCases;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.skillmate.skillmate.modules.goals.GoalRepository;
import com.skillmate.skillmate.modules.users.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DeleteUserUseCase {

  private final UserRepository userRepository;
  private final GoalRepository goalRepository;

  @Transactional
  @CacheEvict(value = { "users", "goals" }, allEntries = true, key = "#id")
  public void execute(String id) {
    if (!userRepository.existsById(id)) {
      throw new RuntimeException("User not found");
    }

    long goalsCount = goalRepository.countByUser_Id(id);
    if (goalsCount > 0) {
      throw new RuntimeException("Cannot delete user: " + goalsCount + " goal(s) are associated with this user");
    }

    userRepository.deleteById(id);
  }
}
