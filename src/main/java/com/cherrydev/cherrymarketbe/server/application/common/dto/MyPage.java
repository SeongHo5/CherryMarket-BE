package com.cherrydev.cherrymarketbe.server.application.common.dto;

import com.github.pagehelper.PageInfo;
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

    public MyPage(PageInfo<T> pageInfo) {
        this.content = pageInfo.getList();
        this.numberOfElements = pageInfo.getSize();
        this.totalElements = pageInfo.getTotal();
        this.totalPages = pageInfo.getPages();
        this.number = pageInfo.getPageNum();
        this.size = pageInfo.getPageSize();
        this.first = pageInfo.isIsFirstPage();
        this.last = pageInfo.isIsLastPage();
    }

}
