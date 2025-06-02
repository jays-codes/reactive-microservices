package jayslabs.microservices.aggregator.service;

import org.springframework.stereotype.Service;

import jayslabs.microservices.aggregator.client.StockServiceClient;
import jayslabs.microservices.aggregator.client.dto.PriceUpdateDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * Service implementation for stock price operations.
 * Handles business logic and delegates to StockServiceClient for external calls.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class StockPriceServiceImpl implements StockPriceService {

    private final StockServiceClient stockServiceClient;

    @Override
    public Flux<PriceUpdateDTO> streamPriceUpdates() {
        return stockServiceClient.priceUpdatesStream();
    }
} 