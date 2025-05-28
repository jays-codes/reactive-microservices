package jayslabs.microservices.customers.service;

import jayslabs.microservices.customers.client.dto.CustomerInfoDTO;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerInfoDTO> findCustomerInfoById(Integer id);
    // Add more service methods as needed
} 