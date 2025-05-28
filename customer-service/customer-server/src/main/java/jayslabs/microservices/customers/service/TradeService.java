package jayslabs.microservices.customers.service;

import jayslabs.microservices.customers.client.dto.StockTradeRequest;
import jayslabs.microservices.customers.client.dto.StockTradeResponse;
import reactor.core.publisher.Mono;

public interface TradeService {
    Mono<StockTradeResponse> processTrade(Integer customerId, StockTradeRequest request);
    // Add more trade-related service methods as needed
} 