package com.cherrydev.cherrymarketbe.order.dto.responses;

import java.util.List;

public record OrderSummaryList(List<OrderInfoResponse> ordersSummary) {
}
