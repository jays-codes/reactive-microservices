package jayslabs.section11.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import jayslabs.section11.entity.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

}
