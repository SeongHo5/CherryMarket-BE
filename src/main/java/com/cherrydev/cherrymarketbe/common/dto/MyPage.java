package com.cherrydev.cherrymarketbe.common.dto;

import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

@Value
public class MyPage<T> {

    List<T> content;

    int numberOfElements;
    long totalElements;
    int totalPages;
    int number;
    int size;
    boolean first;
    boolean last;

    public MyPage(PageImpl<T> page) {
        this.content = page.getContent();
        this.numberOfElements = page.getNumberOfElements();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.first = page.isFirst();
        this.last = page.isLast();
    }

    public MyPage(Page<T> page){
        this.content = page.getContent();
        this.numberOfElements = page.getNumberOfElements();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.number = page.getNumber();
        this.size = page.getSize();
        this.first = page.isFirst();
        this.last = page.isLast();
    }

}
