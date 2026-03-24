package com.github.chaunguyentruongan.warehouse_cdnsg.exception;

import java.time.LocalDate;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(exception = ResourceNotFoundException.class)
    public ResponseEntity<ResponseExceptionDTO> handleNotFoundException(ResourceNotFoundException ex,
            HttpServletRequest request) {
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO();
        responseExceptionDTO.setHttpStatus(404);
        responseExceptionDTO.setError(ex.getMessage());
        responseExceptionDTO.setTime(LocalDate.now());
        responseExceptionDTO.setPath(request.getPathInfo());

        return ResponseEntity.status(responseExceptionDTO.getHttpStatus()).body(responseExceptionDTO);
    }

    @ExceptionHandler(exception = SqlDuplicateException.class)
    public ResponseEntity<ResponseExceptionDTO> handleGlobalException(SqlDuplicateException ex,
            HttpServletRequest request) {
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO();
        responseExceptionDTO.setHttpStatus(409);
        responseExceptionDTO.setError(ex.getMessage());
        responseExceptionDTO.setTime(LocalDate.now());
        responseExceptionDTO.setPath(request.getPathInfo());

        return ResponseEntity.status(responseExceptionDTO.getHttpStatus()).body(responseExceptionDTO);
    }

    @ExceptionHandler(exception = RuntimeException.class)
    public ResponseEntity<ResponseExceptionDTO> handleGlobalException(RuntimeException ex,
            HttpServletRequest request) {
        ResponseExceptionDTO responseExceptionDTO = new ResponseExceptionDTO();
        responseExceptionDTO.setHttpStatus(404);
        responseExceptionDTO.setError(ex.getMessage());
        responseExceptionDTO.setTime(LocalDate.now());
        responseExceptionDTO.setPath(request.getPathInfo());

        return ResponseEntity.status(responseExceptionDTO.getHttpStatus()).body(responseExceptionDTO);
    }
}
