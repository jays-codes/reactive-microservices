package jayslabs.section11;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import jayslabs.section11.dto.ProductDTO;
import reactor.test.StepVerifier;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=section11")
public class ServerSentEventTest {

    private static final Logger log = LoggerFactory.getLogger(ServerSentEventTest.class);

    @Autowired
    private WebTestClient client;

    //test streamProducts endpoint
    @Test
    public void testServerSentEvents() {
        log.info("testServerSentEvent()");
        this.client.get()
            .uri("/products/stream/50")
            .accept(MediaType.TEXT_EVENT_STREAM)
            .exchange()
            .expectStatus().is2xxSuccessful()
            .returnResult(ProductDTO.class)
            .getResponseBody()
            .take(3)
            .doOnNext(p -> log.info("product: {}", p))
            .collectList()
            .as(StepVerifier::create)
            .assertNext(products -> {
                log.info("products: {}", products);
                Assertions.assertEquals(3, products.size());
                Assertions.assertTrue(products.stream().allMatch(p -> p.price() <= 50));
                })
            .expectComplete()
            .verify();
    }
}
