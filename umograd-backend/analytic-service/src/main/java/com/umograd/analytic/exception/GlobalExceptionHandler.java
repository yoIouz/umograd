package com.umograd.analytic.exception;

import com.umograd.analytic.security.AuthenticationHolder;
import com.umograd.analytic.service.SystemLogService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Map;

@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    private final SystemLogService systemLogService;

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex, HttpServletRequest request) {
        Long userId = AuthenticationHolder.getUserId();
        String endpoint = request.getRequestURI();
        String errorMessage = ex.getMessage() != null ? ex.getMessage() : "Неопределнное исключение";
        systemLogService.logError(userId, AuthenticationHolder.getUsername(), endpoint,errorMessage);
        log.error(errorMessage);
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Внутренняя ошибка сервера. Инцидент зафиксирован в системном журнале."));
    }
}
