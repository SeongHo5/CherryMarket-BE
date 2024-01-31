package com.cherrydev.cherrymarketbe.server.domain.core.dto;

import com.github.pagehelper.PageInfo;
import lombok.Builder;
import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import java.util.List;

@Builder
public record MyPage<T>(
        List<T> content,
        int numberOfElements,
        long totalElements,
        int totalPages,
        int number,
        int size,
        boolean first,
        boolean last
) {
    public MyPage(PageInfo<T> pageInfo) {
        this(
                pageInfo.getList(),
                pageInfo.getSize(),
                pageInfo.getTotal(),
                pageInfo.getPages(),
                pageInfo.getPageNum(),
                pageInfo.getPageSize(),
                pageInfo.isIsFirstPage(),
                pageInfo.isIsLastPage()
        );
    }
}

