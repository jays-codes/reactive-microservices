package jayslabs.microservices.customers.service;

import org.springframework.stereotype.Service;

import jayslabs.microservices.customers.client.dto.CustomerInfoDTO;
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
    public Mono<CustomerInfoDTO> findCustomerInfoById(Integer id) {
        return this.customerRepository.findById(id)
        .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
        .flatMap(this::getCustomerInfoWithPortfolioItem);
        
    }

    private Mono<CustomerInfoDTO> getCustomerInfoWithPortfolioItem(Customer customer) {
        return this.portfolioItemRepository.findAllByCustomerId(customer.getId())
        .collectList()
        .map(items -> CustomerMapper.toCustomerInfoDTO(customer, items));
    }

} 