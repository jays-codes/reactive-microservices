package jayslabs.microservices.customers.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import jayslabs.microservices.customers.entity.PortfolioItem;

@Repository
public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {
    // Custom query methods can be defined here
} 