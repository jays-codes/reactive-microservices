package jayslabs.section9;

import org.springframework.web.reactive.function.client.WebClient;
import org.junit.jupiter.api.Test;
import jayslabs.section9.dto.ProductDTO;
import reactor.test.StepVerifier;
import java.time.Duration;
import java.util.Map;
public class Section09HeaderTest extends AbstractWebClient{

    private final WebClient client = createWebClient(
        b -> b.defaultHeader("caller-id", "order-service")
    );

    @Test
    public void defaultHeaderTest(){
        this.client.get()
        .uri("/lec04/product/{id}", 1)
        //.header("caller-id", "order-service")
        .retrieve()
        .bodyToMono(ProductDTO.class)
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    }

    @Test
    public void headerMapTest(){

        var headerMap = Map.of(
            "caller-id", "order-service",
            "some-key", "some-value");

        this.client.get()
        .uri("/lec04/product/{id}", 1)
        .headers(h -> h.setAll(headerMap))
        .retrieve()
        .bodyToMono(ProductDTO.class)
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    }
}
