package jayslabs.demo03.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import jayslabs.demo03.entity.Customer;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {
    Flux<Customer> findByName(String name);

    //Assignment: find all customers whose email ends with ke@gmail.com
    Flux<Customer> findByEmailEndingWith(String email);
}
