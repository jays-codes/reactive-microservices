package jayslabs.microservices.customers.repository;

import jayslabs.microservices.customers.entity.Customer;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
    // Custom query methods can be defined here
}
