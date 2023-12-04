package com.cherrydev.cherrymarketbe.common.advice;

import com.cherrydev.cherrymarketbe.common.exception.*;
import com.cherrydev.cherrymarketbe.common.exception.dto.ExceptionDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.MyBatisSystemException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.FAILED_HTTP_ACTION;


/**
 * Exception 처리를 위한 Advice
 * <p>
 * 자세한 로그는 서버 로그로만 기록하고,
 * <p>
 * 클라이언트에게는 상태 코드와 간단한 메세지만 전달하도록 구성한다.
 */
@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionAdvice {

    @ExceptionHandler({AuthException.class})
    protected ResponseEntity<ExceptionDto> authException(AuthException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(ex.getStatusCode()).
                body(new ExceptionDto(ex.getStatusCode(), ex.getMessage()));
    }

    @ExceptionHandler({DuplicatedException.class})
    protected ResponseEntity<ExceptionDto> duplicatedException(DuplicatedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(ex.getStatusCode()).
                body(new ExceptionDto(ex.getStatusCode(), ex.getMessage()));
    }

    @ExceptionHandler({NotFoundException.class})
    protected ResponseEntity<ExceptionDto> notFoundException(NotFoundException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(ex.getStatusCode()).
                body(new ExceptionDto(ex.getStatusCode(), ex.getMessage()));
    }

    @ExceptionHandler({ServiceFailedException.class})
    protected ResponseEntity<ExceptionDto> serviceFailedException(ServiceFailedException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(ex.getStatusCode()).
                body(new ExceptionDto(ex.getStatusCode(), ex.getMessage()));
    }

    /**
     * Parameter Validation 실패 시
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ExceptionDto> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(400).
                body(new ExceptionDto(400, "Constraint Failed"));
    }

    @ExceptionHandler({HttpClientErrorException.class})
    protected ResponseEntity<ExceptionDto> httpClientErrorException(HttpClientErrorException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(FAILED_HTTP_ACTION.getStatusCode()).
                body(new ExceptionDto(FAILED_HTTP_ACTION.getStatusCode(), FAILED_HTTP_ACTION.getMessage()));
    }

    @ExceptionHandler({NullPointerException.class})
    protected ResponseEntity<ExceptionDto> nullPointerException(NullPointerException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(404).
                body(new ExceptionDto(404, "Not Found / Please Contact to Admin"));
    }

    @ExceptionHandler({MyBatisSystemException.class})
    protected ResponseEntity<ExceptionDto> myBatisSystemException(MyBatisSystemException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(500).
                body(new ExceptionDto(500, "DB Error / Please Contact to Admin"));
    }
}
