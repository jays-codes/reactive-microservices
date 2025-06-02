package jayslabs.microservices.aggregator.service;

import jayslabs.microservices.aggregator.client.dto.CustomerInfoDTO;
import jayslabs.microservices.aggregator.client.dto.StockTradeResponse;
import jayslabs.microservices.aggregator.client.dto.TradeRequest;
import reactor.core.publisher.Mono;

/**
 * Service interface for customer portfolio operations.
 * Aggregates customer data with real-time stock prices to provide portfolio views.
 */
public interface CustomerPortfolioService {

    /**
     * Gets enriched customer portfolio with current stock prices
     * @param customerId the customer ID
     * @return customer info with current portfolio valuation
     */
    Mono<CustomerInfoDTO> getCustomerPortfolio(Integer customerId);

    Mono<StockTradeResponse> processTrade(Integer custId, TradeRequest request);
    
    } 