package jayslabs.microservices.customers.dto;

import jayslabs.microservices.customers.domain.Ticker;

public record Holding(Ticker ticker, Integer quantity) {} 