package jayslabs.demo03.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import jayslabs.demo03.entity.Customer;
import reactor.core.publisher.Flux;

public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
    Flux<Customer> findByName(String name);
}
