package com.cherrydev.cherrymarketbe.goodsReview.service;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
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
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.ALREADY_EXIST_REVIEW;
import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.REVIEW_NOT_ALLOWED_BEFORE_DELIVERY;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.PAGE_HEADER;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.createPage;
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
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> findAll(final Pageable pageable) {
        MyPage<GoodsReviewInfoDto> infoPage = createPage(pageable, goodsReviewMapper::findAll);
        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> findAllByGoodsId(final Pageable pageable, final Long goodsId) {
        MyPage<GoodsReviewInfoDto> infoPage = createPage(pageable, () -> goodsReviewMapper.findAllByGoodsId(goodsId));
        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> findAllByOrderId(final Pageable pageable,final Long ordersId) {
        MyPage<GoodsReviewInfoDto> infoPage = createPage(pageable, () -> goodsReviewMapper.findAllByOrderId(ordersId));
        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Override
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> findAllByUser(final Pageable pageable,final  Long userId) {
        MyPage<GoodsReviewInfoDto> infoPage = createPage(pageable, () -> goodsReviewMapper.findAllByUserId(userId));
        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
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
