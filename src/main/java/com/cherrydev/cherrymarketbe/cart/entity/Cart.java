package com.cherrydev.cherrymarketbe.cart.entity;

import com.cherrydev.cherrymarketbe.goods.entity.Goods;
import com.cherrydev.cherrymarketbe.goods.enums.SalesStatus;
import com.cherrydev.cherrymarketbe.order.enums.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Cart {

    private Long cartId;

    private Long accountId;

    private int quantity;

    private Goods goods;

    public static class CartUpdateBuilder {
        private Long cartId;
        private Integer quantity;

        public CartUpdateBuilder cartId(Long cartId) {
            this.cartId = cartId;
            return this;
        }

        public CartUpdateBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public Cart build() {
            Cart cart = new Cart();
            cart.cartId = this.cartId;
            cart.quantity = this.quantity;

            return cart;
        }
    }

    public static class CartCreateBuilder {
        private Long accountId;
        private Integer quantity;
        private Goods goods;


        public CartCreateBuilder accountId(Long accountId) {
            this.accountId = accountId;
            return this;
        }

        public CartCreateBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public CartCreateBuilder goods(Goods goods) {
            this.goods = goods;
            return this;
        }

        public Cart build() {
            Cart cart = new Cart();
            cart.accountId = this.accountId;
            cart.quantity = this.quantity;
            cart.goods = this.goods;

            return cart;
        }
    }

    public static CartUpdateBuilder builderUpdate(){
        return new CartUpdateBuilder();
    }

    public static CartCreateBuilder builderCreate(){
        return new CartCreateBuilder();
    }


    public boolean isGoodsAvailable() {
        return SalesStatus.ON_SALE.name().equals(this.getGoods().getSalesStatus());
    }



}
