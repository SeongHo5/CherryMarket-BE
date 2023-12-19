package com.cherrydev.cherrymarketbe.goods.service;

import com.cherrydev.cherrymarketbe.common.dto.MyPage;
import com.cherrydev.cherrymarketbe.common.service.FileService;
import com.cherrydev.cherrymarketbe.common.utils.PagingUtil;
import com.cherrydev.cherrymarketbe.goods.dto.*;
import com.cherrydev.cherrymarketbe.goods.exception.DiscontinuedGoodsException;
import com.cherrydev.cherrymarketbe.goods.exception.InsufficientStockException;
import com.cherrydev.cherrymarketbe.goods.exception.NotFoundException;
import com.cherrydev.cherrymarketbe.goods.exception.OnSaleGoodsException;
import com.cherrydev.cherrymarketbe.goods.repository.GoodsMapper;
import com.github.pagehelper.page.PageMethod;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.cherrydev.cherrymarketbe.goods.exception.enums.GoodsExceptionStatus.*;
import static com.cherrydev.cherrymarketbe.common.utils.PagingUtil.createPage;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GoodsService {

    private final GoodsMapper goodsMapper;
    private final FileService fileService;

    /* Insert */
    @Transactional
    public void save(GoodsDto goodsDto, List<MultipartFile> images) {

        if(images.size() != 3) {
            throw new IllegalArgumentException(INVALID_IMAGE_COUNT.getMessage());
        }

        // 파일 업로드
        fileService.uploadMultipleFiles(images, "goods");

        // 새 상품이 입력 되었을 떄 동일한 Code를 갖고 있다면 기존 상품의 판매 상태를 변경
        goodsMapper.updateStatusWhenNewGoods(goodsDto.getGoodsCode(), "DISCONTINUANCE");
        goodsMapper.save(goodsDto);
    }

    /* Select */
    public MyPage<GoodsBasicInfoResponseDto> findAll(final Pageable pageable, String sortBy) {

        MyPage<GoodsBasicInfoResponseDto> pageResult = PagingUtil.createPage(pageable, () -> {
            PageMethod.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());

            return goodsMapper.findAll(sortBy).stream()
                           .map(this::convertToDiscountCalcDto)
                           .collect(Collectors.toList());
        });

        if(pageResult.getContent().isEmpty()) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }
        return pageResult;
    }


    public GoodsBasicInfoResponseDto findBasicInfo(Long goodsId) {
        GoodsBasicInfoDto basicInfoDto = goodsMapper.findBasicInfo(goodsId);

        if(basicInfoDto == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        // 할인된 금액 계산
        Integer discountedPrice = calculateDiscountedPrice(basicInfoDto.getPrice(), basicInfoDto.getDiscountRate());

        // 할인된 금액과 함께 Dto 반환
        return GoodsBasicInfoResponseDto.builder()
                       .goodsId(basicInfoDto.getGoodsId())
                       .goodsName(basicInfoDto.getGoodsName())
                       .goodsCode(basicInfoDto.getGoodsCode())
                       .description(basicInfoDto.getDescription())
                       .price(basicInfoDto.getPrice())
                       .discountRate(basicInfoDto.getDiscountRate())
                       .discountedPrice(discountedPrice)
                       .build();
    }

    public MyPage<GoodsBasicInfoResponseDto> findByCategoryId(final Pageable pageable, Long categoryId, String sortBy) {

        MyPage<GoodsBasicInfoResponseDto> pageResult = createPage(pageable, () -> {
            PageMethod.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());

            return goodsMapper.findByCategoryId(categoryId, sortBy).stream()
                           .map(this::convertToDiscountCalcDto)
                           .collect(Collectors.toList());
        });

        if(pageResult.getContent().isEmpty()) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        return pageResult;
    }

    public ToCartResponseDto findToCart(Long goodsId) {

        ToCartResponseDto toCartResponseDto = goodsMapper.findToCart(goodsId);

        return ToCartResponseDto.builder()
                       .goodsId(toCartResponseDto.getGoodsId())
                       .goodsName(toCartResponseDto.getGoodsName())
                       .price(toCartResponseDto.getPrice())
                       .inventory(toCartResponseDto.getInventory())
                       .storageType(toCartResponseDto.getStorageType())
                       .salesStatus(toCartResponseDto.getSalesStatus())
                       .discountRate(toCartResponseDto.getDiscountRate())
                       .discountedPrice(toCartResponseDto.getDiscountRate())
                       .build();
    }

    public GoodsDetailResponseDto findDetailById(Long goodsId) {
        GoodsDetailDto goodsDetailDto = goodsMapper.findDetailById(goodsId);

        if(goodsDetailDto == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        Integer discountedPrice = (goodsDetailDto.getDiscountRate() != null) ? goodsDetailDto.getPrice() - (goodsDetailDto.getPrice() * goodsDetailDto.getDiscountRate() / 100) : null;

        return GoodsDetailResponseDto.builder()
                       .goodsId(goodsDetailDto.getGoodsId())
                       .goodsCode(goodsDetailDto.getGoodsCode())
                       .goodsName(goodsDetailDto.getGoodsName())
                       .description(goodsDetailDto.getDescription())
                       .price(goodsDetailDto.getPrice())
                       .inventory(goodsDetailDto.getInventory())
                       .storageType(goodsDetailDto.getStorageType())
                       .capacity(goodsDetailDto.getCapacity())
                       .expDate(goodsDetailDto.getExpDate())
                       .allergyInfo(goodsDetailDto.getAllergyInfo())
                       .originPlace(goodsDetailDto.getOriginPlace())
                       .salesStatus(goodsDetailDto.getSalesStatus())
                       .discountRate(goodsDetailDto.getDiscountRate())
                       .discountedPrice(discountedPrice)
                       .makerName(goodsDetailDto.getMakerName())
                       .build();
    }

    public GoodsDetailResponseDto findDetailByCode(String goodsCode) {
        GoodsDetailDto goodsDetailDto = goodsMapper.findDetailByCode(goodsCode);

        if(goodsDetailDto == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        Integer discountedPrice = calculateDiscountedPrice(goodsDetailDto.getPrice(), goodsDetailDto.getDiscountRate());

        return GoodsDetailResponseDto.builder()
                       .goodsId(goodsDetailDto.getGoodsId())
                       .goodsCode(goodsDetailDto.getGoodsCode())
                       .goodsName(goodsDetailDto.getGoodsName())
                       .description(goodsDetailDto.getDescription())
                       .price(goodsDetailDto.getPrice())
                       .inventory(goodsDetailDto.getInventory())
                       .storageType(goodsDetailDto.getStorageType())
                       .capacity(goodsDetailDto.getCapacity())
                       .expDate(goodsDetailDto.getExpDate())
                       .allergyInfo(goodsDetailDto.getAllergyInfo())
                       .originPlace(goodsDetailDto.getOriginPlace())
                       .salesStatus(goodsDetailDto.getSalesStatus())
                       .discountRate(goodsDetailDto.getDiscountRate())
                       .discountedPrice(discountedPrice)
                       .makerName(goodsDetailDto.getMakerName())
                       .build();
    }

    public MyPage<GoodsBasicInfoResponseDto> findByName(final Pageable pageable, String goodsName, String sortBy) {
        MyPage<GoodsBasicInfoResponseDto> pageResult = PagingUtil.createPage(pageable, () -> {
            PageMethod.startPage(pageable.getPageNumber() + 1, pageable.getPageSize());

            return goodsMapper.findByName(goodsName, sortBy).stream()
                           .map(this::convertToDiscountCalcDto)
                           .collect(Collectors.toList());
        });

        if(pageResult.getContent().isEmpty()) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        return pageResult;
    }

    public boolean findInventory(Long goodsId, int requestedQuantity) {
        GoodsInventoryResponseDto goodsInventory = goodsMapper.findInventoryByGoodsId(goodsId);

        System.out.println("상태 : " + goodsInventory.getSalesStatus());

        if(goodsInventory == null) {
            // 상품 조회 실패시 예외 발생
            throw new NotFoundException(GOODS_NOT_FOUND);
        } else if(goodsInventory.getSalesStatus().equals("DISCONTINUANCE")) {
            // 단종된 상품 조회 시 예외 발생
            throw new DiscontinuedGoodsException(DISCONTINUED_GOODS);
        } else if(goodsInventory.getSalesStatus().equals("PAUSE")) {
            // 중단된 상품 조회 시 예외 발생
            throw new DiscontinuedGoodsException(PAUSE_GOODS);
        } else if(goodsInventory.getInventory() < requestedQuantity) {
            // 상품 재고 부족시 예외 발생
            throw new InsufficientStockException(INSUFFICIENT_STOCK);
        }

        return true;
    }

    public List<GoodsBasicInfoResponseDto> findNewGoods() {
        List<GoodsBasicInfoDto> basicInfoDtoList = goodsMapper.findNewGoods();

        if(basicInfoDtoList == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        List<GoodsBasicInfoResponseDto> responseDtoList = new ArrayList<>();

        for(GoodsBasicInfoDto basicInfoDto : basicInfoDtoList) {

            // 할인된 금액 계산
            Integer discountedPrice = calculateDiscountedPrice(basicInfoDto.getPrice(), basicInfoDto.getDiscountRate());

            // 할인된 금액과 함께 Dto 반환
            GoodsBasicInfoResponseDto responseDto = GoodsBasicInfoResponseDto.builder()
                                                            .goodsId(basicInfoDto.getGoodsId())
                                                            .goodsName(basicInfoDto.getGoodsName())
                                                            .goodsCode(basicInfoDto.getGoodsCode())
                                                            .description(basicInfoDto.getDescription())
                                                            .price(basicInfoDto.getPrice())
                                                            .discountRate(basicInfoDto.getDiscountRate())
                                                            .discountedPrice(discountedPrice)
                                                            .build();

            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }

    public List<GoodsBasicInfoResponseDto> findDiscountGoods() {
        List<GoodsBasicInfoDto> basicInfoDtoList = goodsMapper.findDiscountGoods();

        if(basicInfoDtoList == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        List<GoodsBasicInfoResponseDto> responseDtoList = new ArrayList<>();

        for(GoodsBasicInfoDto basicInfoDto : basicInfoDtoList) {

            // 할인된 금액 계산
            Integer discountedPrice = calculateDiscountedPrice(basicInfoDto.getPrice(), basicInfoDto.getDiscountRate());

            // 할인된 금액과 함께 Dto 반환
            GoodsBasicInfoResponseDto responseDto = GoodsBasicInfoResponseDto.builder()
                                                            .goodsId(basicInfoDto.getGoodsId())
                                                            .goodsName(basicInfoDto.getGoodsName())
                                                            .goodsCode(basicInfoDto.getGoodsCode())
                                                            .description(basicInfoDto.getDescription())
                                                            .price(basicInfoDto.getPrice())
                                                            .discountRate(basicInfoDto.getDiscountRate())
                                                            .discountedPrice(discountedPrice)
                                                            .build();

            responseDtoList.add(responseDto);
        }
        return responseDtoList;
    }


    /* Update */
    @Transactional
    public List<GoodsDto> updateDiscountByMaker(Long discountId, Long makerId) {
        goodsMapper.updateDiscountByMaker(discountId, makerId);

        List<GoodsDto> updatedGoodsList = goodsMapper.findByMakerForDiscount(makerId);
        if(updatedGoodsList.isEmpty()) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        return updatedGoodsList;
    }

    @Transactional
    public List<GoodsDto> updateDiscountByCategory(Long discountId, Long categoryId) {
        goodsMapper.updateDiscountByCategory(discountId, categoryId);

        List<GoodsDto> updatedGoodsList = goodsMapper.findByCategoryForDiscount(categoryId);
        if(updatedGoodsList.isEmpty()) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        return updatedGoodsList;
    }

    @Transactional
    public GoodsDto updateDiscountByGoodsId(Long discountId, Long goodsId) {
        GoodsDto goodsInfo = goodsMapper.findByGoodsIdForDiscount(goodsId);

        // 상품이 존재하지 않을 경우
        if(goodsInfo == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }
        // 단종된 상품일 경우
        if("DISCONTINUANCE".equals(goodsInfo.getSalesStatus())) {
            throw new DiscontinuedGoodsException(DISCONTINUED_GOODS);
        }
        goodsMapper.updateDiscountByGoodsId(discountId, goodsId);
        return goodsInfo;
    }

    @Transactional
    public void updateGoodsInventory(Long goodsId, int quantity) {
        goodsMapper.updateGoodsInventory(goodsId, quantity);
    }


    /* Delete */
    @Transactional
    public void deleteById(Long goodsId) {

        GoodsDto goodsInfo = goodsMapper.findById(goodsId);

        // 상품이 존재하지 않을 경우
        if(goodsInfo == null) {
            throw new NotFoundException(GOODS_NOT_FOUND);
        }

        // 판매중인 상품일 경우
        if("ON_SALE".equals(goodsInfo.getSalesStatus())) {
            throw new OnSaleGoodsException(ON_SALE_GOODS);
        }

        goodsMapper.deleteById(goodsId);
    }

    // 페이징 처리를 위한 메소드
    private GoodsBasicInfoResponseDto convertToDiscountCalcDto(GoodsBasicInfoDto basicInfoDto) {

        Integer discountedPrice = calculateDiscountedPrice(basicInfoDto.getPrice(), basicInfoDto.getDiscountRate());

        return GoodsBasicInfoResponseDto.builder()
                       .goodsId(basicInfoDto.getGoodsId())
                       .goodsName(basicInfoDto.getGoodsName())
                       .goodsCode(basicInfoDto.getGoodsCode())
                       .description(basicInfoDto.getDescription())
                       .price(basicInfoDto.getPrice())
                       .discountRate(basicInfoDto.getDiscountRate())
                       .discountedPrice(discountedPrice)
                       .build();
    }

    // 할인된 금액 계산
    private Integer calculateDiscountedPrice(Integer price, Integer discountRate) {
        if(discountRate != null && discountRate > 0) {
            return price - (price * discountRate / 100);
        } else {
            return price;
        }
    }
}
