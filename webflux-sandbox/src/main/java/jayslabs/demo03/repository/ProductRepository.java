package jayslabs.demo03.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import jayslabs.demo03.entity.Product;
import reactor.core.publisher.Flux;

public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

    Flux<Product> findByPriceBetween(int min, int max);
}
