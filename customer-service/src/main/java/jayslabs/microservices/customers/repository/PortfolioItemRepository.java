package jayslabs.microservices.customers.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import jayslabs.microservices.customers.domain.Ticker;
import jayslabs.microservices.customers.entity.PortfolioItem;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface PortfolioItemRepository extends ReactiveCrudRepository<PortfolioItem, Integer> {
    Flux<PortfolioItem> findAllByCustomerId(Integer customerId);

    Mono<PortfolioItem> findByCustomerIdAndTicker(Integer customerId, Ticker ticker);   

} 