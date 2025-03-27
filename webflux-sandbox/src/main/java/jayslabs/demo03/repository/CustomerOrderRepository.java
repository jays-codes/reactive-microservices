package jayslabs.demo03.repository;

import java.util.UUID;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import jayslabs.demo03.entity.CustomerOrder;

@Repository
public interface CustomerOrderRepository extends ReactiveCrudRepository<CustomerOrder, UUID> {

    // @Query("SELECT p.* FROM customer c INNER JOIN customer_order co ON c.id = co.customer_id INNER JOIN product p ON co.product_id = p.id 
    // WHERE c.name = :name")
    // Flux<CustomerOrder> findAllByCustomerName(String name);
}
