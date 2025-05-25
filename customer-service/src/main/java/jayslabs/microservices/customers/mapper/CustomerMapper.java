package jayslabs.microservices.customers.mapper;

import java.util.List;
import java.util.stream.Collectors;

import jayslabs.microservices.customers.dto.CustomerInfo;
import jayslabs.microservices.customers.dto.Holding;
import jayslabs.microservices.customers.entity.Customer;
import jayslabs.microservices.customers.entity.PortfolioItem;

public class CustomerMapper {
    public static CustomerInfo toCustomerInfo(Customer customer, List<PortfolioItem> portfolioItems) {
        return new CustomerInfo(
            customer.getId(),
            customer.getName(),
            customer.getBalance(),
            portfolioItems.stream()
            .map(pi -> new Holding(pi.getTicker(), pi.getQuantity()))
            .collect(Collectors.toList())
        );
    }
} 