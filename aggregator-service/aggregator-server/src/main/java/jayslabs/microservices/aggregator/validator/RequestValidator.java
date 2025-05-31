package jayslabs.microservices.aggregator.validator;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import jayslabs.microservices.aggregator.client.dto.TradeRequest;
import jayslabs.microservices.aggregator.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;


public class RequestValidator {

    public static UnaryOperator<Mono<TradeRequest>> validate(){
        return mono -> mono.filter(hasTicker())
        .switchIfEmpty(ApplicationExceptions.missingTicker())
        .filter(hasTradeAction())
        .switchIfEmpty(ApplicationExceptions.missingTradeAction())
        .filter(isValidQuantity())
        .switchIfEmpty(ApplicationExceptions.invalidQuantity())
        .map(dto -> dto);
    }

    public static Predicate<TradeRequest> hasTicker(){
        return dto -> Objects.nonNull(dto.ticker());
    }

    public static Predicate<TradeRequest> hasTradeAction(){
        return dto -> Objects.nonNull(dto.action());
    }

    public static Predicate<TradeRequest> isValidQuantity(){
        return dto -> Objects.nonNull(dto.quantity()) && dto.quantity() > 0;
    }
}
