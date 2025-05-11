package jayslabs.section9;

import org.springframework.web.reactive.function.client.WebClient;
import org.junit.jupiter.api.Test;
import jayslabs.section9.dto.ProductDTO;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import java.time.Duration;

public class Section09StreamTest extends AbstractWebClient{

    private final WebClient client = createWebClient();

    @Test
    public void streamingResponseTest(){
        this.client.get()
        .uri("/lec02/product/stream")
        .retrieve()
        .bodyToFlux(ProductDTO.class)
        //.take(Duration.ofSeconds(4))
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    }


}