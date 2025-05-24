package jayslabs.microservices.customers.dto;

import java.util.List;

public record CustomerInfo(
    Integer id, 
    String name, 
    Integer balance, 
    List<Holding> holdings) {} 