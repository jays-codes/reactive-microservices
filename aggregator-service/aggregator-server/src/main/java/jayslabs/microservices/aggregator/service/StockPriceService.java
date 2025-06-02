package jayslabs.microservices.aggregator.service;

import jayslabs.microservices.aggregator.client.dto.PriceUpdateDTO;
import reactor.core.publisher.Flux;

/**
 * Service interface for stock price operations.
 * Handles business logic for stock price retrieval and streaming.
 */
public interface StockPriceService {

    /**
     * Streams real-time price updates for all stocks.
     * 
     * @return stream of price updates
     */
    Flux<PriceUpdateDTO> streamPriceUpdates();
} 