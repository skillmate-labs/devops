package com.skillmate.skillmate.modules.goals.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.skillmate.skillmate.modules.goals.dto.AIGoalSuggestionDTO;
import com.skillmate.skillmate.modules.goals.dto.AIGoalSuggestionRequestDTO;
import com.skillmate.skillmate.modules.goals.dto.GoalDTO;
import com.skillmate.skillmate.modules.goals.useCases.CreateGoalUseCase;
import com.skillmate.skillmate.modules.goals.useCases.DeleteGoalUseCase;
import com.skillmate.skillmate.modules.goals.useCases.GetAIGoalSuggestionUseCase;
import com.skillmate.skillmate.modules.goals.useCases.GetGoalUseCase;
import com.skillmate.skillmate.modules.goals.useCases.ListGoalsUseCase;
import com.skillmate.skillmate.modules.goals.useCases.UpdateGoalUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/goals")
@RequiredArgsConstructor
@Validated
public class GoalController {

  private final CreateGoalUseCase createGoalUseCase;
  private final GetGoalUseCase getGoalUseCase;
  private final ListGoalsUseCase listGoalsUseCase;
  private final UpdateGoalUseCase updateGoalUseCase;
  private final DeleteGoalUseCase deleteGoalUseCase;
  private final GetAIGoalSuggestionUseCase getAIGoalSuggestionUseCase;

  @PostMapping
  public ResponseEntity<GoalDTO> create(@Valid @RequestBody GoalDTO goalDTO,
      Authentication authentication) {
    String currentUserId = authentication.getName();
    GoalDTO created = createGoalUseCase.execute(goalDTO, currentUserId);
    return ResponseEntity.status(HttpStatus.CREATED).body(created);
  }

  @GetMapping("/{id}")
  public ResponseEntity<GoalDTO> getById(@PathVariable String id) {
    GoalDTO goal = getGoalUseCase.execute(id);
    return ResponseEntity.status(HttpStatus.OK).body(goal);
  }

  @GetMapping
  public ResponseEntity<Page<GoalDTO>> list(
      @RequestParam(required = false) String userId,
      @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
    Page<GoalDTO> goals = listGoalsUseCase.execute(userId, pageable);
    return ResponseEntity.status(HttpStatus.OK).body(goals);
  }

  @PutMapping("/{id}")
  public ResponseEntity<GoalDTO> update(@PathVariable String id,
      @Valid @RequestBody GoalDTO goalDTO, Authentication authentication) {
    String currentUserId = authentication.getName();
    GoalDTO updated = updateGoalUseCase.execute(id, goalDTO, currentUserId);
    return ResponseEntity.status(HttpStatus.OK).body(updated);
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    deleteGoalUseCase.execute(id);
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

  @PostMapping("/ai-suggestion")
  public ResponseEntity<AIGoalSuggestionDTO> getAISuggestion(
      @Valid @RequestBody AIGoalSuggestionRequestDTO request) {
    AIGoalSuggestionDTO suggestion = getAIGoalSuggestionUseCase.execute(request.getExperience(), request.getSkill());
    return ResponseEntity.status(HttpStatus.OK).body(suggestion);
  }
}
