package jayslabs.section11.service;

import jayslabs.section11.dto.ProductDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface ProductService {
    Flux<ProductDTO> saveProducts(Flux<ProductDTO> flux);
    Mono<Long> getProductsCount();
    Flux<ProductDTO> getAllProducts();
    Mono<ProductDTO> saveProduct(Mono<ProductDTO> dto);
    Flux<ProductDTO> streamProducts();
}
