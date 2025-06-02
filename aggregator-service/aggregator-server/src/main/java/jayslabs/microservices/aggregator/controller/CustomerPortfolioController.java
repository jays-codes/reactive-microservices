package jayslabs.microservices.aggregator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jayslabs.microservices.aggregator.client.dto.CustomerInfoDTO;
import jayslabs.microservices.aggregator.client.dto.StockTradeResponse;
import jayslabs.microservices.aggregator.client.dto.TradeRequest;
import jayslabs.microservices.aggregator.service.CustomerPortfolioService;
import jayslabs.microservices.aggregator.validator.RequestValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * REST controller for customer portfolio operations.
 * Provides endpoints for retrieving customer portfolios and processing trades.
 */
@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
@Slf4j
public class CustomerPortfolioController {

    private final CustomerPortfolioService custPortSrvc;

    /**
     * Retrieves customer portfolio information with current stock prices.
     * 
     * @param custId the customer ID
     * @return customer portfolio information
     */
    @GetMapping("/{custId}")
    public Mono<CustomerInfoDTO> getCustomerPortfolio(@PathVariable Integer custId) {
        return custPortSrvc.getCustomerPortfolio(custId);
    }

    /**
     * Processes a trade request for a customer.
     * 
     * @param customerId the customer ID
     * @param requestMono the trade request
     * @return the trade response
     */
    @PostMapping("/{custId}/trade")
    public Mono<StockTradeResponse> processTrade(
            @PathVariable Integer custId, 
            @RequestBody Mono<TradeRequest> requestMono) {
        
        return requestMono
            .transform(RequestValidator.validate())
            .flatMap(request -> custPortSrvc.processTrade(custId, request));
    }
} 