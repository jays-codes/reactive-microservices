package jayslabs.microservices.customers.service;

import org.springframework.stereotype.Service;

import jayslabs.microservices.customers.dto.CustomerInfo;
import jayslabs.microservices.customers.entity.Customer;
import jayslabs.microservices.customers.exceptions.ApplicationExceptions;
import jayslabs.microservices.customers.mapper.CustomerMapper;
import jayslabs.microservices.customers.repository.CustomerRepository;
import jayslabs.microservices.customers.repository.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    
    private final CustomerRepository customerRepository;
    private final PortfolioItemRepository portfolioItemRepository;

    @Override
    public Mono<CustomerInfo> findCustomerInfoById(Integer id) {
        return this.customerRepository.findById(id)
        .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
        .flatMap(this::getCustomerInfo);
        
    }

    private Mono<CustomerInfo> getCustomerInfo(Customer customer) {
        return this.portfolioItemRepository.findAllByCustomerId(customer.getId())
        .collectList()
        .map(items -> CustomerMapper.toCustomerInfo(customer, items));
    }

} 