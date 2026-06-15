package com.github.chaunguyentruongan.warehouse_cdnsg.exception;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = ResourceNotFoundException.class)
    public ResponseEntity<ResponseExceptionDTO> handleNotFoundException(ResourceNotFoundException ex,
            HttpServletRequest request) {
        log.warn("ResourceNotFoundException at [{}]: {}", request.getRequestURI(), ex.getMessage());
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO();
        responseExceptionDTO.setHttpStatus(404);
        responseExceptionDTO.setError(ex.getMessage());
        responseExceptionDTO.setMessage(ex.getMessage());
        responseExceptionDTO.setTime(LocalDate.now());
        responseExceptionDTO.setPath(request.getPathInfo());

        return ResponseEntity.status(responseExceptionDTO.getHttpStatus()).body(responseExceptionDTO);
    }

    @ExceptionHandler(exception = SqlDuplicateException.class)
    public ResponseEntity<ResponseExceptionDTO> handleGlobalException(SqlDuplicateException ex,
            HttpServletRequest request) {
        log.warn("SqlDuplicateException at [{}]: {}", request.getRequestURI(), ex.getMessage());
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO();
        responseExceptionDTO.setHttpStatus(409);
        responseExceptionDTO.setError(ex.getMessage());
        responseExceptionDTO.setMessage(ex.getMessage());
        responseExceptionDTO.setTime(LocalDate.now());
        responseExceptionDTO.setPath(request.getPathInfo());

        return ResponseEntity.status(responseExceptionDTO.getHttpStatus()).body(responseExceptionDTO);
    }

    @ExceptionHandler(exception = ResourceExistsException.class)
    public ResponseEntity<ResponseExceptionDTO> handleResourceExistsException(
            ResourceExistsException ex,
            HttpServletRequest request) {
        log.warn("ResourceExistsException at [{}]: {}", request.getRequestURI(), ex.getMessage());
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO();
        responseExceptionDTO.setHttpStatus(409);
        responseExceptionDTO.setError(ex.getMessage());
        responseExceptionDTO.setMessage(ex.getMessage());
        responseExceptionDTO.setTime(LocalDate.now());
        responseExceptionDTO.setPath(request.getPathInfo());

        return ResponseEntity.status(responseExceptionDTO.getHttpStatus()).body(responseExceptionDTO);
    }

    @ExceptionHandler(exception = TokenException.class)
    public ResponseEntity<ResponseExceptionDTO> handleTokenException(TokenException ex,
            HttpServletRequest request) {
        log.warn("TokenException at [{}]: {}", request.getRequestURI(), ex.getMessage());
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO();
        responseExceptionDTO.setHttpStatus(401);
        responseExceptionDTO.setError(ex.getMessage());
        responseExceptionDTO.setMessage(ex.getMessage());
        responseExceptionDTO.setTime(LocalDate.now());
        responseExceptionDTO.setPath(request.getPathInfo());

        return ResponseEntity.status(responseExceptionDTO.getHttpStatus()).body(responseExceptionDTO);
    }

    @ExceptionHandler(exception = IllegalArgumentException.class)
    public ResponseEntity<ResponseExceptionDTO> handleIllegalArgumentException(IllegalArgumentException ex,
            HttpServletRequest request) {
        log.warn("IllegalArgumentException at [{}]: {}", request.getRequestURI(), ex.getMessage());
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO();
        responseExceptionDTO.setHttpStatus(400);
        responseExceptionDTO.setError(ex.getMessage());
        responseExceptionDTO.setMessage(ex.getMessage());
        responseExceptionDTO.setTime(LocalDate.now());
        responseExceptionDTO.setPath(request.getPathInfo());

        return ResponseEntity.status(responseExceptionDTO.getHttpStatus()).body(responseExceptionDTO);
    }

    @ExceptionHandler(exception = RuntimeException.class)
    public ResponseEntity<ResponseExceptionDTO> handleGlobalException(RuntimeException ex,
            HttpServletRequest request) {
        log.error("RuntimeException at [{}]: {}", request.getRequestURI(), ex.getMessage(), ex);
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO();
        responseExceptionDTO.setHttpStatus(404);
        responseExceptionDTO.setError(ex.getMessage());
        responseExceptionDTO.setMessage(ex.getMessage());
        responseExceptionDTO.setTime(LocalDate.now());
        responseExceptionDTO.setPath(request.getPathInfo());

        return ResponseEntity.status(responseExceptionDTO.getHttpStatus()).body(responseExceptionDTO);
    }
}
