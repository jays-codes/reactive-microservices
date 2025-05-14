package jayslabs.section9;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Section09BearerAuthTest extends AbstractWebClient {
    
    //private final WebClient client = createWebClient();

    private final WebClient client = createWebClient(b -> b.defaultHeaders(
        h -> h.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9")));
        
    @Test
    void testBearerAuth() {
        this.client.get()
        .uri("/lec08/product/{id}", 1)
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    }
}
