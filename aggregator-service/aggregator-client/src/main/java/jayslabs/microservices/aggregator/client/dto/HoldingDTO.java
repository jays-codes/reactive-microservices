package jayslabs.microservices.aggregator.client.dto;

import jayslabs.microservices.common.domain.Ticker;

public record HoldingDTO(
    Ticker ticker, 
    Integer quantity) {} 