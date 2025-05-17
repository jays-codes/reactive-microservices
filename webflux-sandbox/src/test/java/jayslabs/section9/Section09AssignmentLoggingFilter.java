package jayslabs.section9;

import java.util.UUID;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import jayslabs.section9.dto.ProductDTO;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class Section09AssignmentLoggingFilter extends AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(Section09AssignmentLoggingFilter.class);

    private final WebClient client = createWebClient(
        b -> b.filter(tokenGenerator())
        .filter(loggingFilter()));
        
    @Test
    void testExchangeFilter() {

        //generate token 5 times
        
        Flux.range(1,5)
        .flatMap(i -> this.client.get()
        .uri("/lec09/product/{id}", i)
        //enable logging for even numbers
        .attribute("enableLogging", i % 2 == 0)
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

    //create a filter that logs the HTTP, method, URL, bearer token
    private ExchangeFilterFunction loggingFilter() {
        return (req, next) -> {
            var enabled = (Boolean) req.attributes().getOrDefault("enableLogging", false);
            if (enabled) {
                
            log.info("""
                HTTP method: {}, 
                URI: {}, 
                Headers: {}, 
                Bearer Token: {}
                """, 
                req.method(), 
                req.url(), 
                req.headers(), 
                Optional.ofNullable(req.headers().get("Authorization"))
                    .filter(h -> h.get(0).startsWith("Bearer "))
                    .map(h -> h.get(0))
                    .orElse("Not available"));
            }
            return next.exchange(req);
        };
    }
}
