package jayslabs.section9;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Section09BasicAuthTest extends AbstractWebClient {
    
    //private final WebClient client = createWebClient();

    private final WebClient client = createWebClient(b -> b.defaultHeaders(
        h -> h.setBasicAuth("java", "secret")));
        
    @Test
    void testBasicAuth() {
        this.client.get()
        .uri("/lec07/product/{id}", 1)
        //.header("Authorization", "Basic " + Base64.getEncoder().encodeToString("user:password".getBytes()))
        .retrieve()
        .bodyToMono(String.class)
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    }
}
