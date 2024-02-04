package com.cherrydev.cherrymarketbe.server.infrastructure.repository.goods;

import com.cherrydev.cherrymarketbe.server.domain.goods.entity.Goods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoodsRepository extends JpaRepository<Goods, Long> {



}
