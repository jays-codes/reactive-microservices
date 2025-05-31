package jayslabs.microservices.aggregator.client;

import java.time.Duration;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import jayslabs.microservices.aggregator.client.dto.PriceUpdateDTO;
import jayslabs.microservices.aggregator.client.dto.StockPriceResponse;
import jayslabs.microservices.common.domain.Ticker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class StockServiceClient {
    
    private final WebClient stockWebClient;

    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return stockWebClient
            .get()
            .uri("/stocks/{ticker}/price", ticker.name())
            .retrieve()
            .bodyToMono(StockPriceResponse.class);
    }
    
    public Flux<PriceUpdateDTO> getPriceUpdates() {
        return stockWebClient
            .get()
            .uri("/stock/price-stream")
            .accept(MediaType.APPLICATION_NDJSON)
            .retrieve()
            .bodyToFlux(PriceUpdateDTO.class);

            // .timeout(Duration.ofSeconds(5))
            // .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
            // .doOnNext(update -> log.debug("Received price update for {}: {}", ticker, update.price()))
            // .doOnError(error -> log.error("Failed to get price updates for ticker: {}", ticker, error));
    }

    // public Mono<StockPriceResponse> getCurrentPrice(Ticker ticker) {
    //     return stockWebClient
    //         .get()
    //         .uri("/stocks/{ticker}/price", ticker.name())
    //         .retrieve()
    //         .onStatus(status -> status.is4xxClientError(), 
    //             response -> ApplicationExceptions.invalidTradeRequest("Invalid ticker: " + ticker))
    //         .onStatus(status -> status.is5xxServerError(), 
    //             response -> Mono.error(new RuntimeException("Stock service unavailable")))
    //         .bodyToMono(StockPriceResponse.class)
    //         .timeout(Duration.ofSeconds(5))
    //         .retryWhen(Retry.backoff(3, Duration.ofMillis(100)))
    //         .doOnNext(price -> log.debug("Retrieved price for {}: {}", ticker, price.price()))
    //         .doOnError(error -> log.error("Failed to get price for ticker: {}", ticker, error));
    // }
    
    public Mono<Boolean> isMarketOpen() {
        return stockWebClient
            .get()
            .uri("/stocks/market/status")
            .retrieve()
            .bodyToMono(Boolean.class)
            .timeout(Duration.ofSeconds(3))
            .defaultIfEmpty(false)
            .doOnNext(isOpen -> log.debug("Market status: {}", isOpen ? "OPEN" : "CLOSED"));
    }
} 