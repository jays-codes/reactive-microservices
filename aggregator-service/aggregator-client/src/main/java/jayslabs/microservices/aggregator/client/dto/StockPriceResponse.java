package jayslabs.microservices.aggregator.client.dto;

import java.time.LocalDateTime;

import jayslabs.microservices.common.domain.Ticker;

public record StockPriceResponse(
    Ticker ticker,
    Integer price,
    LocalDateTime time
) {
}
