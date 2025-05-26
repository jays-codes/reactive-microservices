package jayslabs.microservices.customers.mapper;

import java.util.List;
import java.util.stream.Collectors;

import jayslabs.microservices.customers.dto.CustomerInfoDTO;
import jayslabs.microservices.customers.dto.HoldingDTO;
import jayslabs.microservices.customers.entity.Customer;
import jayslabs.microservices.customers.entity.PortfolioItem;

public class CustomerMapper {
    public static CustomerInfoDTO toCustomerInfo(Customer customer, List<PortfolioItem> portfolioItems) {
        return new CustomerInfoDTO(
            customer.getId(),
            customer.getName(),
            customer.getBalance(),
            portfolioItems.stream()
            .map(pi -> new HoldingDTO(pi.getTicker(), pi.getQuantity()))
            .collect(Collectors.toList())
        );
    }
} 