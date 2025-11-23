package com.skillmate.skillmate.exception;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import com.skillmate.skillmate.config.LocaleUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  private final MessageSource messageSource;

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, Object>> handleValidationExceptions(
      MethodArgumentNotValidException ex, WebRequest request) {
    final Locale locale = LocaleUtils.normalize(getCurrentLocale(request));

    Map<String, Object> errors = new HashMap<>();
    Map<String, String> fieldErrors = new HashMap<>();

    ex.getBindingResult().getFieldErrors().forEach((error) -> {
      String message = resolveMessage(error, locale);
      fieldErrors.put(error.getField(), message);
    });

    String validationFailedMessage = messageSource.getMessage(
        "error.validation.failed", null, "Validation failed", locale);

    errors.put("status", HttpStatus.BAD_REQUEST.value());
    errors.put("message", validationFailedMessage);
    errors.put("errors", fieldErrors);

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  private String resolveMessage(FieldError error, Locale locale) {
    String defaultMessage = error.getDefaultMessage();
    if (defaultMessage != null && defaultMessage.startsWith("{") && defaultMessage.endsWith("}")) {
      String messageKey = defaultMessage.substring(1, defaultMessage.length() - 1);
      return messageSource.getMessage(messageKey, error.getArguments(), defaultMessage, locale);
    }
    return defaultMessage;
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
    log.error("Runtime exception occurred", ex);

    Map<String, Object> error = new HashMap<>();
    error.put("status", HttpStatus.BAD_REQUEST.value());
    error.put("message", ex.getMessage());

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex, WebRequest request) {
    log.error("Unexpected exception occurred", ex);

    Locale locale = LocaleUtils.normalize(getCurrentLocale(request));
    String errorMessage = messageSource.getMessage(
        "error.unexpected", null, "An unexpected error occurred", locale);

    Map<String, Object> error = new HashMap<>();
    error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
    error.put("message", errorMessage);

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
  }

  private Locale getCurrentLocale(WebRequest request) {
    Locale locale = LocaleContextHolder.getLocale();
    return locale != null ? locale : request.getLocale();
  }
}
