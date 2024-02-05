package com.cherrydev.cherrymarketbe.reviewLike.repository;

import com.cherrydev.cherrymarketbe.reviewLike.entity.ReviewLike;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewLikeMapper {

    void save(ReviewLike entity);

    boolean existLike(ReviewLike reviewLike);

    void delete(ReviewLike entity);

    Long countLike(Long reviewId);
}
