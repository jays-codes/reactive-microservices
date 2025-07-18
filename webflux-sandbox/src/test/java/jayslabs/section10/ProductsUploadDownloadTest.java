package jayslabs.section10;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jayslabs.section10.dto.ProductDTO;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
public class ProductsUploadDownloadTest {

    private static final Logger log = LoggerFactory.getLogger(ProductsUploadDownloadTest.class);

    private final ProductClient client = new ProductClient();

    @Test
    public void testUploadProducts() {
        // var flux = Flux.just(new ProductDTO(null, "iphone", 100))
        //     .delayElements(Duration.ofSeconds(10));

        var flux = Flux.range(1, 1_000_000)
            .map(i -> new ProductDTO(null, "product - #" + i, i));
            //.delayElements(Duration.ofSeconds(2));


        this.client.uploadProducts(flux)
            .doOnNext(response -> log.info("received: {}", response))
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    public void testDownloadProducts() {
        this.client.downloadProducts()
            .doOnNext(product -> log.info("received: {}", product))
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    public void testDownloadProductsToFile() {
        this.client.downloadProductsToFile()
            .doOnNext(product -> log.info("received: {}", product))
            .then()
            .as(StepVerifier::create)
            .verifyComplete();
    }

    @Test
    public void testFluxFileWriter() {
        this.client.downloadProducts()
            .map(ProductDTO::toString)
            .as(flux -> FluxFileWriter.create(flux, Path.of("products2.txt")))
            .as(StepVerifier::create)
            .expectComplete()
            .verify();
    }
}