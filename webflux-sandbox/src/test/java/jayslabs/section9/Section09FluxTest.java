package jayslabs.section9;

import org.springframework.web.reactive.function.client.WebClient;
import org.junit.jupiter.api.Test;
import jayslabs.section9.dto.ProductDTO;
import reactor.test.StepVerifier;
import java.time.Duration;
import reactor.core.publisher.Mono;
public class Section09FluxTest extends AbstractWebClient{

    private final WebClient client = createWebClient();

    @Test
    public void postBodyValueTest(){

        var product = new ProductDTO(null, 
        "Sec09FluxTest - product", 1099);

        this.client.post()
        .uri("/lec03/product")
        .bodyValue(product)
        .retrieve()
        .bodyToMono(ProductDTO.class)
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    }

    @Test
    public void postBodyPublisherTest(){

        var monoprod = Mono.fromSupplier(() -> new ProductDTO(null, 
        "Sec09FluxTest - mono product", 1099));

        this.client.post()
        .uri("/lec03/product")
        .body(monoprod, ProductDTO.class)
        .retrieve()
        .bodyToMono(ProductDTO.class)
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    } 
}
