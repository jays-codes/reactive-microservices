package jayslabs.section9;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;
import org.slf4j.LoggerFactory;

import jayslabs.section9.dto.ProductDTO;

import org.slf4j.Logger;
import org.springframework.web.reactive.function.client.ClientRequest;

import reactor.test.StepVerifier;

import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import reactor.core.publisher.Flux;

public class Section09ExchangeFilterTest extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(Section09ExchangeFilterTest.class);

    private final WebClient client = createWebClient(b -> b.filter(tokenGenerator()));
        
    @Test
    void testExchangeFilter() {
        Flux.range(1,5)
        .flatMap(i -> this.client.get()
        .uri("/lec09/product/{id}", i)
        .retrieve()
        .bodyToMono(ProductDTO.class))
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    }

    //to generate a token for call to GET
    private ExchangeFilterFunction tokenGenerator() {
        return (req, next) -> {
            var token = UUID.randomUUID().toString().replace("-", "");
            log.info("generated token: {}", token);
            //req.headers().setBearerAuth(token);
            var modifiedRequest = ClientRequest.from(req).headers(h -> h.setBearerAuth(token)).build();
            return next.exchange(modifiedRequest);
        };
    }
}
