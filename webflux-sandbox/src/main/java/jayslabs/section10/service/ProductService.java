package jayslabs.section10.service;

import jayslabs.section10.dto.ProductDTO;
import reactor.core.publisher.Flux;

public interface ProductService {
    Flux<ProductDTO> saveProducts(Flux<ProductDTO> flux);
}
