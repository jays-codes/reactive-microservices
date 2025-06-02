package jayslabs.microservices.aggregator.client;

import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException.BadRequest;
import org.springframework.web.reactive.function.client.WebClientResponseException.NotFound;

import jayslabs.microservices.aggregator.client.dto.CustomerInfoDTO;
import jayslabs.microservices.aggregator.client.dto.StockTradeRequest;
import jayslabs.microservices.aggregator.client.dto.StockTradeResponse;
import jayslabs.microservices.aggregator.exceptions.ApplicationExceptions;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class CustomerServiceClient {

    private final Logger log = LoggerFactory.getLogger(CustomerServiceClient.class);

    private final WebClient client;

    /**
     * Retrieves customer information by ID
     */
    public Mono<CustomerInfoDTO> getCustomerInfo(Integer custId) {
        return client
            .get()
            .uri("/customers/{id}", custId)
            .retrieve()
            .bodyToMono(CustomerInfoDTO.class)
            .onErrorResume(NotFound.class,
            ex -> ApplicationExceptions.customerNotFound(custId));
    }

    /**
     * Processes a trade for a customer
     */
    public Mono<StockTradeResponse> processTrade(
        Integer custId, StockTradeRequest request) {
        return client
            .post()
            .uri("/customers/{id}/trade", custId)
            .bodyValue(request)
            .retrieve()
            .bodyToMono(StockTradeResponse.class)
            .onErrorResume(NotFound.class,
            ex -> ApplicationExceptions.customerNotFound(custId))
            .onErrorResume(BadRequest.class,this::handleException);
    }

    private <T> Mono<T> handleException(BadRequest ex) {
        var pd = ex.getResponseBodyAs(ProblemDetail.class);
        var msg = Objects.nonNull(pd) ? pd.getDetail() : ex.getMessage();
        log.error("Customer Service problem detail: {}", pd);
        return ApplicationExceptions.invalidTradeRequest(msg);
    }
} 