package jayslabs.demo03.repository;

import java.util.UUID;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import jayslabs.demo03.dto.OrderDetailsDTO;
import jayslabs.demo03.entity.CustomerOrder;
import jayslabs.demo03.entity.Product;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerOrderRepository extends ReactiveCrudRepository<CustomerOrder, UUID> {

    //find all the products ordered by the given customer name
    // @Query("SELECT co.* FROM customer c INNER JOIN customer_order co ON c.id = co.customer_id INNER JOIN product p ON co.product_id = p.id WHERE c.name = :name")
    // Flux<CustomerOrder> findAllByCustomerName(String name);

    @Query("""
        SELECT p.* FROM customer c 
        INNER JOIN customer_order co ON c.id = co.customer_id 
        INNER JOIN product p ON co.product_id = p.id 
        WHERE c.name = :name
        """)
    Flux<Product> getProductsOrderedByCustomerName(String name);

    //get order details by product description
    @Query("""
        SELECT 
            co.order_id,
            c.name AS customer_name,
            p.description AS product_name,
            co.amount,
            co.order_date
        FROM
            customer c
            INNER JOIN customer_order co ON c.id = co.customer_id
            INNER JOIN product p ON p.id = co.product_id
        WHERE
            p.description = :description
        ORDER BY co.amount DESC
        """)
    Flux<OrderDetailsDTO> getOrderDetailsByProductDescription(String description);
}
