package jayslabs.section12;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import jayslabs.section11.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

public class ConnectPoolTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(ConnectPoolTest.class);

    private final WebClient client = createWebClient(
        b -> {
            var poolSize = 500;
            var provider = ConnectionProvider.builder("custom")
            .lifo()
            .maxConnections(poolSize)
            .pendingAcquireMaxCount(poolSize)
            .build();

            var httpClient = HttpClient.create(provider)
            .compress(true)
            .keepAlive(true);

            b.clientConnector(new ReactorClientHttpConnector(httpClient));
        }
    );

    @Test
    public void concurrentRequests() throws InterruptedException {
        var max = 260;
        Flux.range(1, max)
        .flatMap(this::getProduct, max)
        .doOnNext(p -> log.info("product: {}", p))
        .collectList()
        .as(StepVerifier::create)
        .assertNext(products -> Assertions.assertEquals(max, products.size()))
        .expectComplete()
        .verify();

        //Thread.sleep(Duration.ofMinutes(1));
    }

    //get product by id
    private Mono<Product> getProduct(int id){
        return this.client.get()
        .uri("/product/{id}", id)
        .retrieve()
        .bodyToMono(Product.class);
    }

}
