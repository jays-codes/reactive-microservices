package jayslabs.section10.controller;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jayslabs.section10.dto.ProductDTO;
import jayslabs.section10.dto.UploadResponseDTO;
import jayslabs.section10.service.ProductService;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("products")
public class ProductController {

    private static final Logger log = LoggerFactory.getLogger(ProductController.class);

    private final ProductService service;

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