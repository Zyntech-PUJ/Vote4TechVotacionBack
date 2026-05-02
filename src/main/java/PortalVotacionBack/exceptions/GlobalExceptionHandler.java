package PortalVotacionBack.exceptions;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(ResourceNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleNotFound(
      ResourceNotFoundException ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
        new ErrorResponse(LocalDateTime.now(), HttpStatus.NOT_FOUND.value(),
            "Not Found", ex.getMessage(), request.getRequestURI()));
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponse> handleBadRequest(
      BadRequestException ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
        new ErrorResponse(LocalDateTime.now(), HttpStatus.BAD_REQUEST.value(),
            "Bad Request", ex.getMessage(), request.getRequestURI()));
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusiness(
      BusinessException ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(
        new ErrorResponse(LocalDateTime.now(), HttpStatus.CONFLICT.value(),
            "Conflict", ex.getMessage(), request.getRequestURI()));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleGeneric(
      Exception ex, HttpServletRequest request) {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
        new ErrorResponse(LocalDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Internal Server Error", ex.getMessage(), request.getRequestURI()));
  }

}
