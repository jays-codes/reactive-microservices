package jayslabs.microservices.customers.dto;

import jayslabs.microservices.customers.domain.Ticker;
import jayslabs.microservices.customers.domain.TradeAction;

public record StockTradeRequest(
    Ticker ticker,
    Integer price,
    Integer quantity,
    TradeAction action
) {} 