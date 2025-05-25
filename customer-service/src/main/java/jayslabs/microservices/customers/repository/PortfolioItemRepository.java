package jayslabs.microservices.customers.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import jayslabs.microservices.customers.entity.PortfolioItem;
import reactor.core.publisher.Flux;

@Repository
public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {
    Flux<PortfolioItem> findAllByCustomerId(Integer customerId);
    // Custom query methods can be defined here
} 