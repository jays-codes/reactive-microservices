package jayslabs.section11.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jayslabs.section11.dto.ProductDTO;
import jayslabs.section11.dto.UploadResponseDTO;
import jayslabs.section11.service.ProductService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;


    @PostMapping(value = "/save")
    public Mono<ProductDTO> saveProduct(@RequestBody Mono<ProductDTO> dto) {
        log.info("invoked: ProductController.saveProduct()");
        return this.service.saveProduct(dto);
    }

    @GetMapping(value = "stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDTO> streamProducts() {
        log.info("invoked: ProductController.streamProducts()");
        return this.service.streamProducts();
    }

    //create an endpoint similar to streamProducts but with path variable representing maxPrice
    @GetMapping(value = "/stream/{maxPrice}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ProductDTO> streamProducts(@PathVariable int maxPrice) {
        log.info("invoked: ProductController.streamProducts() with maxPrice: {}", maxPrice);
        return this.service.streamProducts().filter(p -> p.price() <= maxPrice);
    }

    @PostMapping(value = "/upload", consumes = MediaType.APPLICATION_NDJSON_VALUE)
    public Mono<UploadResponseDTO> uploadProducts(@RequestBody Flux<ProductDTO> products) {
        log.info("invoked: ProductController.uploadProducts()");
        return this.service.saveProducts(
            products
            //.doOnNext(dto -> log.info("processing: {}", dto))
            )
        .then(this.service.getProductsCount())
        .map(count -> new UploadResponseDTO(UUID.randomUUID(), count));
    }

    @GetMapping(value = "/download", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<ProductDTO> getAllProducts() {
        log.info("invoked: ProductController.getAllProducts()");
        return this.service.getAllProducts();
    }
}