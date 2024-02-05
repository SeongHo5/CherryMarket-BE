package com.cherrydev.cherrymarketbe.goodsReview.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.account.entity.Account;
import com.cherrydev.cherrymarketbe.account.service.impl.AccountServiceImpl;
import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.exception.DuplicatedException;
import com.cherrydev.cherrymarketbe.common.exception.ServiceFailedException;
import com.cherrydev.cherrymarketbe.customer.dto.reward.AddRewardRequestDto;
import com.cherrydev.cherrymarketbe.customer.repository.CustomerRewardMapper;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewInfoDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewModifyDto;
import com.cherrydev.cherrymarketbe.goodsReview.dto.GoodsReviewRequestDto;
import com.cherrydev.cherrymarketbe.goodsReview.entity.GoodsReview;
import com.cherrydev.cherrymarketbe.goodsReview.enums.GoodsReviewBest;
import com.cherrydev.cherrymarketbe.goodsReview.repository.GoodsReviewMapper;
import com.cherrydev.cherrymarketbe.goodsReview.utils.BadWordFilter;
import com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static com.cherrydev.cherrymarketbe.common.exception.enums.ExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.PAGE_HEADER;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.createPage;
import static com.cherrydev.cherrymarketbe.notice.enums.DisplayStatus.DELETED;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsReviewServiceImpl implements GoodsReviewService {

    private final GoodsReviewMapper goodsReviewMapper;
    private final CustomerRewardMapper customerRewardMapper;
    private final AccountServiceImpl accountService;

    @Override
    @Transactional
    public void save(final GoodsReviewRequestDto goodsReviewRequestDto, final AccountDetails accountDetails) {
        CheckValidationForInsert(goodsReviewRequestDto,accountDetails);
        goodsReviewMapper.save(goodsReviewRequestDto.toEntity(accountDetails));

        AddRewardRequestDto rewardRequestDto = AddRewardRequestDto.builder()
                .email(accountDetails.getAccount().getEmail())
                .rewardGrantType("REVIEW")
                .amounts(50)
                .earnedAt((LocalDate.now()).toString())
                .expiredAt((LocalDate.now()).plusMonths(6).toString())
                .build();

        Account account = accountService.findAccountByEmail(accountDetails.getUsername());
        customerRewardMapper.save(rewardRequestDto.toEntity(account));
    }

    @Override
    @Transactional
    public ResponseEntity<GoodsReviewInfoDto> update(GoodsReviewModifyDto modifyDto,AccountDetails accountDetails) {

        CheckUserValidation( modifyDto.getOrdersId(),modifyDto.getGoodsId(),accountDetails.getAccount().getAccountId());
        CheckValidationForModify(modifyDto);
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
    public void delete(Long ordersId, Long goodsId, AccountDetails accountDetails) {
        CheckExistReviewForDelete(ordersId, goodsId);
        CheckUserValidation(ordersId, goodsId, accountDetails.getAccount().getAccountId());
        goodsReviewMapper.delete(ordersId, goodsId);

        AddRewardRequestDto rewardRequestDto = AddRewardRequestDto.builder()
                .email(accountDetails.getAccount().getEmail())
                .rewardGrantType("REVIEW")
                .amounts(-50)
                .earnedAt((LocalDate.now()).toString())
                .expiredAt((LocalDate.now()).plusMonths(6).toString())
                .build();

        Account account = accountService.findAccountByEmail(accountDetails.getUsername());
        customerRewardMapper.save(rewardRequestDto.toEntity(account));
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

        List<GoodsReviewInfoDto> getDto = goodsReviewMapper.findAll();
        getDto.forEach(dto -> {
            dto.updateContent(CheckForForbiddenWordsTest(dto.getContent()));
            dto.updateSubject(CheckForForbiddenWordsTest(dto.getSubject()));
        });
        MyPage<GoodsReviewInfoDto> infoPage = createPage(pageable, () -> getDto);

        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> findAllByGoodsId(final Pageable pageable, final Long goodsId) {

        List<GoodsReviewInfoDto> getDto = goodsReviewMapper.findAllByGoodsId(goodsId);
        getDto.forEach(dto -> {
            dto.updateContent(CheckForForbiddenWordsTest(dto.getContent()));
            dto.updateSubject(CheckForForbiddenWordsTest(dto.getSubject()));
        });
        MyPage<GoodsReviewInfoDto> infoPage = createPage(pageable, () -> getDto);

        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }


    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> findAllByOrderId(final Pageable pageable, final Long ordersId) {

        List<GoodsReviewInfoDto> getDto = goodsReviewMapper.findAllByOrderId(ordersId);
        getDto.forEach(dto -> {
            dto.updateContent(CheckForForbiddenWordsTest(dto.getContent()));
            dto.updateSubject(CheckForForbiddenWordsTest(dto.getSubject()));
        });
        MyPage<GoodsReviewInfoDto> infoPage = createPage(pageable, () -> getDto);

        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> findAllByUser(final Pageable pageable, final Long userId) {


        List<GoodsReviewInfoDto> getDto = goodsReviewMapper.findAllByUserId(userId);
        getDto.forEach(dto -> {
            dto.updateContent(CheckForForbiddenWordsTest(dto.getContent()));
            dto.updateSubject(CheckForForbiddenWordsTest(dto.getSubject()));
        });
        MyPage<GoodsReviewInfoDto> infoPage = createPage(pageable, () -> getDto);

        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }

    @Override
    @Transactional(readOnly = true)
    public ResponseEntity<MyPage<GoodsReviewInfoDto>> findAllMyList(Pageable pageable, Long accountId) {

        List<GoodsReviewInfoDto> getDto = goodsReviewMapper.findAllMyList(accountId);
        getDto.forEach(dto -> {
            dto.updateContent(CheckForForbiddenWordsTest(dto.getContent()));
            dto.updateSubject(CheckForForbiddenWordsTest(dto.getSubject()));
        });
        MyPage<GoodsReviewInfoDto> infoPage = createPage(pageable, () -> getDto);

        return ResponseEntity.ok()
                .header(PAGE_HEADER, String.valueOf(infoPage.getTotalPages()))
                .body(infoPage);
    }


    // =============== PRIVATE METHODS =============== //
    private void CheckExistReview(GoodsReview goodsReview) {
        if (goodsReviewMapper.existReview(goodsReview)) {
            throw new DuplicatedException(ALREADY_EXIST_REVIEW);
        }
    }
    private void CheckExistReviewForDelete(Long ordersId, Long goodsId) {
        if (!goodsReviewMapper.existReview(ordersId, goodsId)) {
            throw new DuplicatedException(NOT_FOUND_POST);
        }
    }


    private void CheckUserValidation(Long goodsId, Long ordersId, Long userId) {
        if (!goodsReviewMapper.getUserId(goodsId, ordersId, userId)) {
            throw new ServiceFailedException(NOT_POST_OWNER);
        }
    }

    private void CheckUserValidation(String code, Long userId) {
        if (!goodsReviewMapper.getUserIdByCode(code, userId)) {
            throw new ServiceFailedException(NOT_POST_OWNER);
        }
    }

    private void CheckDelieveryStatus(GoodsReview goodsReview) {
        if (!goodsReviewMapper.checkDeliveryStatus(goodsReview)) {
            throw new ServiceFailedException(REVIEW_NOT_ALLOWED_BEFORE_DELIVERY);
        }
    }

    private String CheckForForbiddenWordsTest(String content) {
        System.out.println("CheckForForbiddenWords start");
        return BadWordFilter.filterAndReplace(content);
    }

    private void CheckValidationForInsert(GoodsReviewRequestDto goodsReviewRequestDto, AccountDetails accountDetails) {

        CheckUserValidation(goodsReviewRequestDto.getOrdersId(),
                            goodsReviewRequestDto.getGoodsId(),
                            accountDetails.getAccount().getAccountId());

        if (goodsReviewRequestDto.getContent() == null || goodsReviewRequestDto.getContent().equals("")) {
            throw new ServiceFailedException(NOT_ALLOWED_EMPTY_CONTENT);
        }
        if (goodsReviewRequestDto.getSubject() == null || goodsReviewRequestDto.getSubject().equals("")) {
            throw new ServiceFailedException(NOT_ALLOWED_EMPTY_SUBJECT);
        }
        CheckExistReview(goodsReviewRequestDto.toEntity());
        CheckDelieveryStatus(goodsReviewRequestDto.toEntity());

    }

    private void CheckValidationForModify(GoodsReviewModifyDto modifydto) {
        if (modifydto.getContent() == null || modifydto.getContent().equals("")) {
            throw new ServiceFailedException(NOT_ALLOWED_EMPTY_CONTENT);
        }
        if (modifydto.getSubject() == null || modifydto.getSubject().equals("")) {
            throw new ServiceFailedException(NOT_ALLOWED_EMPTY_SUBJECT);
        }
    }

}
