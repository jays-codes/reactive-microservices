package jayslabs.microservices.aggregator.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jayslabs.microservices.aggregator.client.dto.PriceUpdateDTO;
import jayslabs.microservices.aggregator.service.StockPriceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;

/**
 * REST controller for stock price streaming operations.
 * Provides endpoints for real-time stock price updates and market data.
 */
@RestController
@RequestMapping("stock")
@RequiredArgsConstructor
@Slf4j
public class StockPriceStreamController {

    private final StockPriceService stockPriceService;

    /**
     * Streams real-time price updates for all stocks.
     * Uses Server-Sent Events for continuous data streaming.
     * 
     * @return stream of price updates
     */
    @GetMapping(value = "/price-stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<PriceUpdateDTO> streamPriceUpdates() {
        return stockPriceService.streamPriceUpdates();
    }
} 