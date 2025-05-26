package jayslabs.microservices.customers.dto;

import jayslabs.microservices.customers.domain.Ticker;

public record HoldingDTO(
    Ticker ticker, 
    Integer quantity) {} 