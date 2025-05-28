package jayslabs.microservices.customers.client.dto;

import jayslabs.microservices.common.domain.Ticker;
import jayslabs.microservices.common.domain.TradeAction;

public record StockTradeRequest(
    Ticker ticker,
    Integer price,
    Integer quantity,
    TradeAction action
) {
    public Integer totalPrice() {
        return price * quantity;
    }
} 