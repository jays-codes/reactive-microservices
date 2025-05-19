package jayslabs.section10.service;
 
import org.springframework.stereotype.Service;

import jayslabs.section10.dto.ProductDTO;
import jayslabs.section10.mapper.EntityDTOMapper;
import jayslabs.section10.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;

    @Override
    public Flux<ProductDTO> saveProducts(Flux<ProductDTO> flux) {
        return this.repo.saveAll(flux.map(EntityDTOMapper::toEntity))
        .map(EntityDTOMapper::toDTO);

        // return flux.map(EntityDTOMapper::toEntity)
        // .as(this.repo::saveAll)
        // .map(EntityDTOMapper::toDTO);
    }

    public Mono<Long> getProductsCount() {
        return this.repo.count();
    }
}
