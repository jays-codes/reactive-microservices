package jayslabs.section10.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import jayslabs.section10.entity.Product;

@Repository
public interface ProductRepository extends ReactiveCrudRepository<Product, Integer> {

}
