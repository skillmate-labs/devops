package com.skillmate.skillmate.modules.database.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.skillmate.skillmate.modules.database.dto.GoalCompatibilityRequestDTO;
import com.skillmate.skillmate.modules.database.dto.JsonResponseDTO;
import com.skillmate.skillmate.modules.database.useCases.CalculateGoalCompatibilityUseCase;
import com.skillmate.skillmate.modules.database.useCases.ConvertGoalToJsonUseCase;
import com.skillmate.skillmate.modules.database.useCases.ExportDatasetToJsonUseCase;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/database")
@RequiredArgsConstructor
@Validated
@Slf4j
public class DatabaseFunctionController {

  private final ConvertGoalToJsonUseCase convertGoalToJsonUseCase;
  private final CalculateGoalCompatibilityUseCase calculateGoalCompatibilityUseCase;
  private final ExportDatasetToJsonUseCase exportDatasetToJsonUseCase;
  private final ObjectMapper objectMapper;

  @GetMapping(value = "/goals/{goalId}/json", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> convertGoalToJson(@PathVariable String goalId) {
    try {
      JsonResponseDTO response = convertGoalToJsonUseCase.execute(goalId);
      return parseJsonResponse(response.getJson(), "Erro ao converter goal para JSON");
    } catch (Exception e) {
      log.error("Erro ao converter goal para JSON", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Erro ao converter goal para JSON", e.getMessage()));
    }
  }

  @PostMapping(value = "/goals/compatibility", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> calculateGoalCompatibility(
      @Valid @RequestBody GoalCompatibilityRequestDTO request) {
    try {
      JsonResponseDTO response = calculateGoalCompatibilityUseCase.execute(
          request.getUserId(),
          request.getGoalId());
      return parseJsonResponse(response.getJson(), "Erro ao calcular compatibilidade");
    } catch (Exception e) {
      log.error("Erro ao calcular compatibilidade", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Erro ao calcular compatibilidade", e.getMessage()));
    }
  }

  @GetMapping(value = "/goals/{goalId}/compatibility", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> calculateGoalCompatibilityForCurrentUser(
      @PathVariable String goalId,
      Authentication authentication) {
    try {
      String currentUserId = authentication.getName();
      JsonResponseDTO response = calculateGoalCompatibilityUseCase.execute(
          currentUserId,
          goalId);
      return parseJsonResponse(response.getJson(), "Erro ao calcular compatibilidade");
    } catch (Exception e) {
      log.error("Erro ao calcular compatibilidade", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Erro ao calcular compatibilidade", e.getMessage()));
    }
  }

  @GetMapping(value = "/export", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<Object> exportDataset() {
    try {
      JsonResponseDTO response = exportDatasetToJsonUseCase.execute();
      return parseJsonResponse(response.getJson(), "Erro ao exportar dataset");
    } catch (Exception e) {
      log.error("Erro ao exportar dataset", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse("Erro ao exportar dataset", e.getMessage()));
    }
  }

  private ResponseEntity<Object> parseJsonResponse(String jsonString, String errorMessage) {
    if (jsonString == null || jsonString.trim().isEmpty()) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(new ErrorResponse(errorMessage, "JSON retornado está vazio"));
    }

    try {
      Object jsonObject = objectMapper.readValue(jsonString, Object.class);
      return ResponseEntity.status(HttpStatus.OK).body(jsonObject);
    } catch (JsonProcessingException e) {
      try {
        String correctedJson = fixDecimalCommas(jsonString);
        Object jsonObject = objectMapper.readValue(correctedJson, Object.class);
        return ResponseEntity.status(HttpStatus.OK).body(jsonObject);
      } catch (JsonProcessingException e2) {
        log.warn("Erro ao fazer parse do JSON mesmo após correção. JSON: {}",
            jsonString.length() > 500 ? jsonString.substring(0, 500) + "..." : jsonString);
        return ResponseEntity.status(HttpStatus.OK)
            .body(new JsonStringResponse(jsonString));
      }
    }
  }

  private String fixDecimalCommas(String json) {
    StringBuilder result = new StringBuilder();
    boolean inString = false;
    boolean escapeNext = false;

    for (int i = 0; i < json.length(); i++) {
      char c = json.charAt(i);

      if (escapeNext) {
        result.append(c);
        escapeNext = false;
        continue;
      }

      if (c == '\\') {
        escapeNext = true;
        result.append(c);
        continue;
      }

      if (c == '"') {
        inString = !inString;
        result.append(c);
        continue;
      }

      if (!inString && c == ',' && i > 0 && i < json.length() - 1) {
        if (Character.isDigit(json.charAt(i - 1)) && Character.isDigit(json.charAt(i + 1))) {
          int beforeIndex = i - 1;
          while (beforeIndex > 0 && Character.isDigit(json.charAt(beforeIndex))) {
            beforeIndex--;
          }
          while (beforeIndex > 0 && Character.isWhitespace(json.charAt(beforeIndex))) {
            beforeIndex--;
          }
          if (beforeIndex >= 0) {
            char beforeChar = json.charAt(beforeIndex);
            if (beforeChar == ':' || beforeChar == ',' || beforeChar == '[' || beforeChar == '{') {
              result.append('.');
              continue;
            }
          }
        }
      }

      result.append(c);
    }

    return result.toString();
  }

  @lombok.Data
  @lombok.AllArgsConstructor
  private static class ErrorResponse {
    private String message;
    private String details;
  }

  @lombok.Data
  @lombok.AllArgsConstructor
  private static class JsonStringResponse {
    private String json;
  }
}
