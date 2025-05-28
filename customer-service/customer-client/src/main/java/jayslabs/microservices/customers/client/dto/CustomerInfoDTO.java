package jayslabs.microservices.customers.client.dto;

import java.util.List;

public record CustomerInfoDTO(
    Integer id, 
    String name, 
    Integer balance, 
    List<HoldingDTO> holdings) {} 