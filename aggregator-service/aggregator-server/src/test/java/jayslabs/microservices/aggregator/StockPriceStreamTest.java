package jayslabs.microservices.aggregator;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jayslabs.microservices.aggregator.client.dto.PriceUpdateDTO;
import jayslabs.microservices.common.domain.Ticker;
import reactor.test.StepVerifier;


public class StockPriceStreamTest extends AbstractIntegrationTest {


    private static final Logger log = LoggerFactory.getLogger(StockPriceStreamTest.class);
    
    @Test
    public void testStockPriceStream() {
        var respBody = resourceToString("stock-service/stock-price-stream-200.jsonl");
        
        mockClient
        .when(HttpRequest.request("/stock/price-stream"))
        .respond(
            HttpResponse.response(respBody)
            .withStatusCode(200)
            //.withContentType(MediaType.create("application", "x-ndjson"))
            .withContentType(MediaType.parse("application/x-ndjson"))

        ); 

        client.get().uri("/stock/price-stream")
        .accept(org.springframework.http.MediaType.TEXT_EVENT_STREAM)
        .exchange()
        .expectStatus().is2xxSuccessful()
        .returnResult(PriceUpdateDTO.class)
        .getResponseBody()
        .doOnNext(e -> log.info("{}", e))
        .as(StepVerifier::create)
        .assertNext(e -> assertThat(e.ticker()).isEqualTo(Ticker.APPLE))
        .assertNext(e -> assertThat(e.ticker()).isEqualTo(Ticker.GOOGLE))
        .verifyComplete();

    }


}
