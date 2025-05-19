package jayslabs.section10.service;

import jayslabs.section10.dto.ProductDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface ProductService {
    Flux<ProductDTO> saveProducts(Flux<ProductDTO> flux);
    Mono<Long> getProductsCount();
}
