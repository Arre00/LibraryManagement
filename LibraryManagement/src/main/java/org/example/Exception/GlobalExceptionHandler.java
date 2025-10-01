package org.example.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class   GlobalExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        ErrorResponse errorResponse = new ErrorResponse(
            "Validation failed",
            HttpStatus.BAD_REQUEST.value(),
            LocalDateTime.now(),
            errors
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleEntityNotFoundException(EntityNotFoundException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            LocalDateTime.now(),
            null
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.CONFLICT.value(),
            LocalDateTime.now(),
            null
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        ErrorResponse errorResponse = new ErrorResponse(
            "An unexpected error occurred",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            LocalDateTime.now(),
            null
        );
        
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    
    public static class ErrorResponse {
        private String message;
        private int status;
        private LocalDateTime timestamp;
        private Map<String, String> validationErrors;
        
        public ErrorResponse(String message, int status, LocalDateTime timestamp, Map<String, String> validationErrors) {
            this.message = message;
            this.status = status;
            this.timestamp = timestamp;
            this.validationErrors = validationErrors;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
        
        public int getStatus() {
            return status;
        }
        
        public void setStatus(int status) {
            this.status = status;
        }
        
        public LocalDateTime getTimestamp() {
            return timestamp;
        }
        
        public void setTimestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
        }
        
        public Map<String, String> getValidationErrors() {
            return validationErrors;
        }
        
        public void setValidationErrors(Map<String, String> validationErrors) {
            this.validationErrors = validationErrors;
        }
    }
}