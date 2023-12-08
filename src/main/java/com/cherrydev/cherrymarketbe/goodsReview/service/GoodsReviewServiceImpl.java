package com.cherrydev.cherrymarketbe.goodsReview.service;

import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewInfoDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewModifyDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
import com.cherrydev.cherrymarketbe.goodsReview.enums.GoodsReviewBest;
import com.cherrydev.cherrymarketbe.goodsReview.repository.GoodsReviewMapper;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.ALREADY_EXIST_REVIEW;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.REVIEW_NOT_ALLOWED_BEFORE_DELIVERY;
import static com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus.DELETED;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsReviewServiceImpl implements GoodsReviewService {

    private final GoodsReviewMapper goodsReviewMapper;

    @Override
    @Transactional
    public void save(final GoodsReviewRequestDto goodsReviewRequestDto) {
        CheckExistReview(goodsReviewRequestDto.toEntity());
        CheckDelieveryStatus(goodsReviewRequestDto.toEntity());
        goodsReviewMapper.save(goodsReviewRequestDto.toEntity());
    }

    @Override
    @Transactional
    public ResponseEntity<GoodsReviewInfoDto> update(GoodsReviewModifyDto modifyDto) {
        GoodsReview goodsReview = goodsReviewMapper.findReivew(modifyDto.getOrdersId(), modifyDto.getGoodsId());

        goodsReview.updateStatus(DELETED);
        goodsReviewMapper.delete(goodsReview.getOrdersId(), goodsReview.getGoodsId());

        GoodsReview newReview = GoodsReview.builder()
                .goodsId(goodsReview.getGoodsId())
                .ordersId(goodsReview.getOrdersId())
                .isBest(GoodsReviewBest.NORMAL)
                .code(goodsReview.getCode())
                .subject(modifyDto.getSubject())
                .content(modifyDto.getContent())
                .status(DisplayStatus.ACTIVE)
                .deleteDate(null)
                .build();

        goodsReviewMapper.update(newReview);
        GoodsReviewInfoDto resultDto = new GoodsReviewInfoDto(goodsReviewMapper.findReivew(newReview.getOrdersId(), newReview.getGoodsId()));

        return ResponseEntity
                .ok()
                .body(resultDto);
    }

    @Override
    @Transactional
    public void delete(Long ordersId, Long goodsId) {
        goodsReviewMapper.delete(ordersId, goodsId);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<GoodsReviewInfoDto> getReview(final Long ordersId, Long goodsId) {
        GoodsReview goodsReview = goodsReviewMapper.findReivew(ordersId, goodsId);
        return ResponseEntity.ok()
                .body(new GoodsReviewInfoDto(goodsReview));
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<GoodsReviewInfoDto>> findAll() {
        List<GoodsReview> goodsReviews = goodsReviewMapper.findAll();
        List<GoodsReviewInfoDto> goodsReviewInfoDtos = GoodsReviewInfoDto.convertToDtoList(goodsReviews);
        return ResponseEntity.ok()
                .body(goodsReviewInfoDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<GoodsReviewInfoDto>> findAllByGoodsId(final Long goodsId) {
        List<GoodsReview> goodsReviews = goodsReviewMapper.findAllByGoodsId(goodsId);
        List<GoodsReviewInfoDto> goodsReviewInfoDtos = GoodsReviewInfoDto.convertToDtoList(goodsReviews);
        return ResponseEntity.ok()
                .body(goodsReviewInfoDtos);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<List<GoodsReviewInfoDto>> findAllByOrderId(final Long ordersId) {
        List<GoodsReview> goodsReviews = goodsReviewMapper.findAllByOrderId(ordersId);
        List<GoodsReviewInfoDto> goodsReviewInfoDtos = GoodsReviewInfoDto.convertToDtoList(goodsReviews);
        return ResponseEntity.ok()
                .body(goodsReviewInfoDtos);
    }

    @Override
    public ResponseEntity<List<GoodsReviewInfoDto>> findAllByUser(Long userId) {
        List<GoodsReview> goodsReviews = goodsReviewMapper.findAllByUserId(userId);
        List<GoodsReviewInfoDto> goodsReviewInfoDtos = goodsReviews.stream()
                .map(goodsReview -> new GoodsReviewInfoDto(goodsReview, userId))
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(goodsReviewInfoDtos);
    }

    @Override
    @Transactional
    public void findAllByLike() {

    }


    // =============== PRIVATE METHODS =============== //
    private void CheckExistReview(GoodsReview goodsReview) {
        if (goodsReviewMapper.existReview(goodsReview)) {
            throw new DuplicatedException(ALREADY_EXIST_REVIEW);
        }
    }

    private void CheckDelieveryStatus(GoodsReview goodsReview) {
        if (!goodsReviewMapper.checkDeliveryStatus(goodsReview)) {
            throw new ServiceFailedException(REVIEW_NOT_ALLOWED_BEFORE_DELIVERY);
        }
    }
}
