package jayslabs.microservices.aggregator.client;

import java.time.Duration;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import jayslabs.microservices.aggregator.client.dto.PriceUpdateDTO;
import jayslabs.microservices.aggregator.client.dto.StockPriceResponse;
import jayslabs.microservices.common.domain.Ticker;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

@RequiredArgsConstructor
public class StockServiceClient {

    private final Logger log = LoggerFactory.getLogger(StockServiceClient.class);

    private final WebClient stockWebClient;
    private Flux<PriceUpdateDTO> priceUpdatesFlux;

    // get the stock price for a given ticker
    public Mono<StockPriceResponse> getStockPrice(Ticker ticker) {
        return stockWebClient
            .get()
            .uri("/stock/{ticker}", ticker.name())
            .retrieve()
            .bodyToMono(StockPriceResponse.class);
    }
    
    public Flux<PriceUpdateDTO> priceUpdatesStream() {
        if (Objects.isNull(priceUpdatesFlux)) {
            priceUpdatesFlux = getPriceUpdates();
        }
        return priceUpdatesFlux;
    }

    private Retry retry(){
        return Retry.fixedDelay(100, Duration.ofSeconds(1))
        .doBeforeRetry(rs -> log.error("Stock Service price stream call failed. retrying: {}", 
        rs.failure().getMessage()));
    }
    
    // cache the price updates for 1 second
    private Flux<PriceUpdateDTO> getPriceUpdates() {
        return stockWebClient
            .get()
            .uri("/stock/price-stream")
            .accept(MediaType.APPLICATION_NDJSON)
            .retrieve()
            .bodyToFlux(PriceUpdateDTO.class)
            .retryWhen(retry())
            .cache(1);

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