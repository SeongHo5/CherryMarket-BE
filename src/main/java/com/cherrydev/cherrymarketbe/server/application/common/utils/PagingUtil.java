package com.cherrydev.cherrymarketbe.server.application.common.utils;

import com.cherrydev.cherrymarketbe.server.domain.core.dto.MyPage;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.page.PageMethod;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Supplier;

public final class PagingUtil {

    public static final String PAGE_HEADER = "X-Total-Count";

    private PagingUtil() {
        throw new IllegalStateException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
    }

    /**
     * PageHelper를 이용한 페이징 처리
     * @param pageable 페이징 정보(page, size)
     * @param querySupplier 쿼리 수행 메서드
     * @return 페이징 처리된 결과
     */
    public static <T> MyPage<T> createPage(Pageable pageable, Supplier<List<T>> querySupplier) {
        PageMethod.startPage(pageable.getPageNumber(), pageable.getPageSize());
        List<T> result = querySupplier.get();
        PageInfo<T> pageInfo = new PageInfo<>(result);
        return new MyPage<>(pageInfo);
    }

    public static <T> MyPage<T> createPage(Pageable pageable, List<T> result) {
        PageInfo<T> pageInfo = new PageInfo<>(result);
        return new MyPage<>(pageInfo);
    }

}
