package com.cherrydev.cherrymarketbe.cart.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class Cart {

    private Long cartId;

    private Long accountId;

    private int quantity;

    private TestGoods goods;

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
        private TestGoods goods;


        public CartCreateBuilder accountId(Long accountId) {
            this.accountId = accountId;
            return this;
        }

        public CartCreateBuilder quantity(int quantity) {
            this.quantity = quantity;
            return this;
        }

        public CartCreateBuilder goods(TestGoods goods) {
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
        return this.getGoods().getSalesStatus().equals("ON_SALE");
    }

}
