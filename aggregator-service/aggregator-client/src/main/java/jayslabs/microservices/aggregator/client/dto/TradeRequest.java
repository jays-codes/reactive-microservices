package jayslabs.microservices.aggregator.client.dto;

import jayslabs.microservices.common.domain.Ticker;
import jayslabs.microservices.common.domain.TradeAction;

public record TradeRequest(
    Ticker ticker,
    TradeAction action,
    Integer quantity
) {
}
