package jayslabs.microservices.aggregator.client.dto;

import java.time.LocalDateTime;

import jayslabs.microservices.common.domain.Ticker;

public record PriceUpdateDTO(
    Ticker ticker,
    Integer price,
    LocalDateTime time
) {
}
