package com.cherrydev.cherrymarketbe.order.dto;

import java.util.List;

public record UserOrderList(List<OrderDetails> userOrders) {
}
