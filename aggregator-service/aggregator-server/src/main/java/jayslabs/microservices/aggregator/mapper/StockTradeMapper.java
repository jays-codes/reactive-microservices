package jayslabs.microservices.aggregator.mapper;

import jayslabs.microservices.aggregator.client.dto.StockTradeRequest;
import jayslabs.microservices.aggregator.client.dto.TradeRequest;

/**
 * Mapper for converting between aggregator DTOs
 */
public class StockTradeMapper {

    /**
     * Maps aggregator TradeRequest to StockTradeRequest with current price
     * @param tradeRequest the aggregator trade request
     * @param currentPrice the current stock price
     * @return stock trade request with price
     */
    public static StockTradeRequest toStockTradeRequest(TradeRequest tradeRequest, Integer currentPrice) {
        return new StockTradeRequest(
            tradeRequest.ticker(),
            currentPrice,
            tradeRequest.quantity(),
            tradeRequest.action()
        );
    }

} 