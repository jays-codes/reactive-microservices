package jayslabs.microservices.customers.client.dto;

import jayslabs.microservices.common.domain.Ticker;
import jayslabs.microservices.common.domain.TradeAction;

public record StockTradeResponse(
    Integer customerId,
    Ticker ticker,
    Integer price,
    Integer quantity,
    TradeAction action,
    Integer totalPrice,
    Integer balance
) {} 