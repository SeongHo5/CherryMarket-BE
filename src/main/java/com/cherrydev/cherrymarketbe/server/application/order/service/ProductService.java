package com.cherrydev.cherrymarketbe.server.application.order.service;

import com.cherrydev.cherrymarketbe.server.domain.account.dto.response.AccountDetails;
import com.cherrydev.cherrymarketbe.server.domain.goods.dto.GoodsInfo;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.responses.OrderReceiptResponse;
import com.cherrydev.cherrymarketbe.server.domain.order.dto.requests.OrderCreationRequest;
import com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods.ProductMapper;
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
