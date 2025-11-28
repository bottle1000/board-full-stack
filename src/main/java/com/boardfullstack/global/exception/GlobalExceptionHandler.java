package com.boardfullstack.global.exception;

import com.boardfullstack.global.common.response.ApiResponse;
import com.boardfullstack.global.common.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    //new CustomException(ErrorCode.POST_NOT_FOUND) 발생 -> Spring이 해당 예외를 처리해줄 메서드 찾음 ->
    // 찾은 후 파라미터의 CustomException e = new CustomException(ErrorCode.POST_NOT_FOUND)를 대입

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        ErrorCode errorCode = e.getErrorCode(); // ErrorCode.POST_NOT_FOUND

        ErrorResponse errorResponse = ErrorResponse.of(errorCode.getCode(), e.getMessage());

        return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.error(errorResponse));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<?>> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {

        List<ErrorResponse.FieldError> fieldErrors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(err -> new ErrorResponse.FieldError(err.getField(), err.getDefaultMessage()))
                .toList();

        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.INVALID_REQUEST.getCode(),
                "유효성 검증에 실패했습니다.",
                fieldErrors
        );

        return ResponseEntity.status(ErrorCode.INVALID_REQUEST.getStatus()).body(ApiResponse.error(errorResponse));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Exception Error : {}", e.getMessage());

        ErrorResponse errorResponse = ErrorResponse.of(
                ErrorCode.INTERNAL_SERVER_ERROR.getCode(),
                ErrorCode.INTERNAL_SERVER_ERROR.getMessage()
        );

        return ResponseEntity.status(ErrorCode.INTERNAL_SERVER_ERROR.getStatus()).body(ApiResponse.error(errorResponse));
    }
}
