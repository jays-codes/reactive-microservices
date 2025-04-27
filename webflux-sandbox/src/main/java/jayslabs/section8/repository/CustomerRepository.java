package jayslabs.section8.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import jayslabs.section8.entity.Customer;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@Repository
public interface CustomerRepository extends ReactiveCrudRepository<Customer, Integer> {

    //add query method to delete customer by id
    @Modifying
    @Query("DELETE FROM customer WHERE id = :id")
    Mono<Boolean> removeById(Integer id);

    //for pagination
    Flux<Customer> findBy(Pageable pageable);
}
