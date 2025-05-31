package jayslabs.microservices.aggregator.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer id) {
        return Mono.error(new CustomerNotFoundException(id));
    }

    public static <T> Mono<T> invalidTradeRequest(String msg) {
        return Mono.error(new InvalidTradeRequestException(msg));
    }

    //missing Ticker
    public static <T> Mono<T> missingTicker() {
        return Mono.error(new InvalidTradeRequestException(
            "Ticker is required"));
    }

    //missing TradeAction
    public static <T> Mono<T> missingTradeAction() {
        return Mono.error(new InvalidTradeRequestException(
            "TradeAction is required"));
    }
    
    //invalid quantity
    public static <T> Mono<T> invalidQuantity() {
        return Mono.error(new InvalidTradeRequestException(
            "Quantity must be greater than 0"));
    }
    
    //invalid price
    
    




} 