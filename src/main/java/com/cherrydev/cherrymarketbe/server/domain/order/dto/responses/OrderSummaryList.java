package com.cherrydev.cherrymarketbe.server.domain.order.dto.responses;

import java.util.List;

public record OrderSummaryList(List<OrderInfoResponse> ordersSummary) {
}
