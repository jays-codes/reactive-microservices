package jayslabs.section12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import jayslabs.section11.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class ConnectPoolTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(ConnectPoolTest.class);

    private final WebClient client = createWebClient();

    @Test
    public void concurrentRequests() {
        var max = 20;
        Flux.range(1, max)
        .flatMap(this::getProduct)
        .doOnNext(p -> log.info("product: {}", p))
        .collectList()
        .as(StepVerifier::create)
        .assertNext(products -> Assertions.assertEquals(max, products.size()))
        .expectComplete()
        .verify();
    }

    //get product by id
    private Mono<Product> getProduct(int id){
        return this.client.get()
        .uri("/product/{id}", id)
        .retrieve()
        .bodyToMono(Product.class);
    }

}
