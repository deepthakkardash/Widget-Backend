package com.example.Widget.in.exception;


import com.example.Widget.in.dto.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<String>> handleUserNotFound(UserNotFoundException e)
    {
        return  new ResponseEntity<>(
                new ApiResponse<>(false,e.getMessage(), null),
                HttpStatus.NOT_FOUND
        );
    }

}
