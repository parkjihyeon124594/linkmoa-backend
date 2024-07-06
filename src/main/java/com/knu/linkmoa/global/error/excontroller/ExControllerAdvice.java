package com.knu.linkmoa.global.error.excontroller;

import com.knu.linkmoa.auth.jwt.dto.response.ApiJwtResponse;
import com.knu.linkmoa.global.error.custom.CookieNotFoundException;
import com.knu.linkmoa.global.error.custom.NotValidTokenException;
import com.knu.linkmoa.global.spec.ApiResponseSpec;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExControllerAdvice {

    @ExceptionHandler(NotValidTokenException.class)
    public ResponseEntity<ApiJwtResponse> handleNotValidTokenException(NotValidTokenException e){

        ApiJwtResponse apiJwtResponse = ApiJwtResponse.builder()
                .status(false)
                .code(401)
                .message(e.getErrorCode().getMessage())
                .tokenStatus(e.getTokenStatus())
                .build();

        return ResponseEntity.status(apiJwtResponse.getCode()).body(apiJwtResponse);
    }
/*
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponseSpec> handleUsernameNotFoundException(UsernameNotFoundException e){

        ApiResponseSpec apiResponseSpec = new ApiResponseSpec(false, 404, e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponseSpec);
    }
*/

    @ExceptionHandler(CookieNotFoundException.class)
    public ResponseEntity<ApiResponseSpec> handleCookieNotFoundException(CookieNotFoundException e){
        ApiResponseSpec apiResponseSpec = new ApiResponseSpec(false, 400, e.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponseSpec);
    }
}