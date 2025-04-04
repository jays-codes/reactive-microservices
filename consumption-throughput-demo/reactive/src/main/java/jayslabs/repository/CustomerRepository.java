package jayslabs.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import jayslabs.entity.Customer;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

}
