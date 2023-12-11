package com.cherrydev.cherrymarketbe.goods.exception.handler;

import com.cherrydev.cherrymarketbe.goods.exception.DiscontinuedGoodsException;
import com.cherrydev.cherrymarketbe.goods.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.goods.exception.OnSaleGoodsException;
import com.cherrydev.cherrymarketbe.goods.exception.response.ExceptionResponse;
import lombok.Getter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Getter
@ControllerAdvice
public class GoodsExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionResponse> handelNotFoundException(NotFoundException e){
        return ResponseEntity
                       .status(e.getStatusCode())
                       .body(new ExceptionResponse(e.getStatusCode(), e.getMessage()));
    }

    @ExceptionHandler(DiscontinuedGoodsException.class)
    public ResponseEntity<ExceptionResponse> handelDiscontinuedGoodsException(DiscontinuedGoodsException e){
        return ResponseEntity
                       .status(e.getStatusCode())
                       .body(new ExceptionResponse(e.getStatusCode(),e.getMessage()));
    }

    @ExceptionHandler(OnSaleGoodsException.class)
    public ResponseEntity<ExceptionResponse> handelOnSaleGoodsException(OnSaleGoodsException e){
        return ResponseEntity
                       .status(e.getStatusCode())
                       .body(new ExceptionResponse(e.getStatusCode(),e.getMessage()));
    }
}
