package jayslabs.section11.service;
 
import org.springframework.stereotype.Service;

import jayslabs.section11.dto.ProductDTO;
import jayslabs.section11.mapper.EntityDTOMapper;
import jayslabs.section11.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final Sinks.Many<ProductDTO> productSink;


    @Override
    public Mono<ProductDTO> saveProduct(Mono<ProductDTO> mono) {
        return mono.map(EntityDTOMapper::toEntity)
        .flatMap(this.repo::save)
        .map(EntityDTOMapper::toDTO)
        .doOnNext(this.productSink::tryEmitNext);
    }

    @Override
    public Flux<ProductDTO> streamProducts() {
        return this.productSink.asFlux();
    }

    @Override
    public Flux<ProductDTO> saveProducts(Flux<ProductDTO> flux) {
        return this.repo.saveAll(flux.map(EntityDTOMapper::toEntity))
        .map(EntityDTOMapper::toDTO);

        // return flux.map(EntityDTOMapper::toEntity)
        // .as(this.repo::saveAll)
        // .map(EntityDTOMapper::toDTO);
    }

    @Override
    public Mono<Long> getProductsCount() {
        return this.repo.count();
    }

    @Override
    public Flux<ProductDTO> getAllProducts() {
        return this.repo.findAll()
        .map(EntityDTOMapper::toDTO);
    }
}
