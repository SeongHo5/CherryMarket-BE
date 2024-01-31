package com.cherrydev.cherrymarketbe.server.application.aop.exception.advice;

import com.cherrydev.cherrymarketbe.server.application.aop.exception.*;
import com.cherrydev.cherrymarketbe.server.application.aop.exception.dto.ErrorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;


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

    @ExceptionHandler({ApplicationException.class})
    protected ResponseEntity<ErrorDto> applicationException(ApplicationException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(ex.getStatusCode()).
                body(new ErrorDto(ex.getStatusCode(), ex.getMessage()));
    }

    /**
     * Parameter Validation 실패 시
     */
    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity<ErrorDto> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(400).
                body(new ErrorDto(400, "Parameter Condition Not Satisfied"));
    }

    @ExceptionHandler({HttpClientErrorException.class})
    protected ResponseEntity<ErrorDto> httpClientErrorException(HttpClientErrorException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(ExceptionStatus.FAILED_HTTP_ACTION.getStatusCode()).
                body(new ErrorDto(ExceptionStatus.FAILED_HTTP_ACTION.getStatusCode(), ExceptionStatus.FAILED_HTTP_ACTION.getMessage()));
    }

    @ExceptionHandler({NullPointerException.class})
    protected ResponseEntity<ErrorDto> nullPointerException(NullPointerException ex) {
        log.error(ex.getMessage());
        return ResponseEntity.
                status(404).
                body(new ErrorDto(404, "Not Found / Please Contact to Admin"));
    }

//    @ExceptionHandler({MyBatisSystemException.class})
//    protected ResponseEntity<ExceptionDto> myBatisSystemException(MyBatisSystemException ex) {
//        log.error(ex.getMessage());
//        return ResponseEntity.
//                status(500).
//                body(new ExceptionDto(500, "DB Error / Please Contact to Admin"));
//    }
}
