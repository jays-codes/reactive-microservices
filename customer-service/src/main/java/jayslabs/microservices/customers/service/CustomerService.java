package jayslabs.microservices.customers.service;

import jayslabs.microservices.customers.dto.CustomerInfo;
import reactor.core.publisher.Mono;

public interface CustomerService {
    Mono<CustomerInfo> findCustomerInfoById(Integer id);
    // Add more service methods as needed
} 