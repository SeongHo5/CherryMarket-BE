package com.cherrydev.cherrymarketbe.order.service;

import com.cherrydev.cherrymarketbe.account.dto.AccountDetails;
import com.cherrydev.cherrymarketbe.order.domain.GoodsInfo;
import com.cherrydev.cherrymarketbe.order.domain.PaymentDetailsInfo;
import com.cherrydev.cherrymarketbe.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.order.dto.requests.OrderCreationRequest;
import com.cherrydev.cherrymarketbe.order.entity.ProductDetails;
import com.cherrydev.cherrymarketbe.order.repository.ProductMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.ExecutorType;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;

    private final SqlSessionFactory sqlSessionFactory;

    @Transactional
    public void createOrderDetail(AccountDetails accountDetails,
                                  OrderReceiptResponse requestDto,
                                  Long orderId
    ) {
        try (SqlSession sqlSession = sqlSessionFactory.openSession(ExecutorType.BATCH)) {
            ProductMapper mapper = sqlSession.getMapper(ProductMapper.class);
            requestDto.goodsInfo()
                    .forEach(goodsInfo -> mapper.save(
                                    new OrderCreationRequest()
                                            .create(
                                                    accountDetails,
                                                    goodsInfo,
                                                    orderId
                                            )
                            )
                    );
            sqlSession.commit();
        }
    }

    public List<GoodsInfo> getGoodsInfo(String orderCode) {
        return productMapper.findByOrderCode(orderCode);
    }


}
