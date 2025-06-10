package jayslabs.microservices.aggregator;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.mockserver.model.RegexBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import jayslabs.microservices.aggregator.client.dto.TradeRequest;
import jayslabs.microservices.common.domain.Ticker;
import jayslabs.microservices.common.domain.TradeAction;

public class CustomerTradeTest extends AbstractIntegrationTest {
    private static final Logger log = LoggerFactory.getLogger(CustomerTradeTest.class);

    @Test
    public void testCustomerTrade() {
        
        // given
        mockCustomerTrade("customer-service/customer-trade-200.json", 200);

        var tradeReq = new TradeRequest(Ticker.APPLE, TradeAction.BUY, 3);

        
        postTrade(tradeReq, HttpStatus.OK)
        .jsonPath("$.customerId").isEqualTo(1)
        .jsonPath("$.ticker").isEqualTo("APPLE")
        .jsonPath("$.price").isEqualTo(300)
        .jsonPath("$.quantity").isEqualTo(3)
        .jsonPath("$.action").isEqualTo("BUY")
        .jsonPath("$.totalPrice").isEqualTo(900)
        .jsonPath("$.balance").isEqualTo(9100);
    }

    @Test
    public void testCustomerTradeInsufficientBalance() {
        
        // given
        mockCustomerTrade("customer-service/customer-trade-400.json", 400);

        var tradeReq = new TradeRequest(Ticker.APPLE, TradeAction.BUY, 1000);

        
        postTrade(tradeReq, HttpStatus.BAD_REQUEST)
        .jsonPath("$.detail").isEqualTo("Customer [id=1] has insufficient balance");
    }

    @Test
    public void inputValidationTest() {

        //given
        mockCustomerTrade("customer-service/customer-trade-400.json", 400);

        var missingTickerTradeReq = new TradeRequest(null, TradeAction.BUY, 1000);
        var missingTradeActionTradeReq = new TradeRequest(Ticker.APPLE, null, 1000);
        var invalidQuantityTradeReq = new TradeRequest(Ticker.APPLE, TradeAction.BUY, 0);

        postTrade(missingTickerTradeReq, HttpStatus.BAD_REQUEST)
        .jsonPath("$.detail").isEqualTo("Ticker is required");

        postTrade(missingTradeActionTradeReq, HttpStatus.BAD_REQUEST)
        .jsonPath("$.detail").isEqualTo("TradeAction is required");

        postTrade(invalidQuantityTradeReq, HttpStatus.BAD_REQUEST)
        .jsonPath("$.detail").isEqualTo("Quantity must be greater than 0");
    }

    private void mockCustomerTrade(String path, int respCode){
        //mock stock-service price response
        mockStockPriceResponse("stock-service/stock-price-200.json", Ticker.APPLE, 200);
    
        //mock customer-service trade response
        var respBody = resourceToString(path);
        mockClient.when(
            HttpRequest.request("/customers/1/trade")
            .withMethod("POST")
            .withBody(RegexBody.regex(".*\"price\":300.*"))
        )
        .respond(
            HttpResponse.response(respBody)
            .withStatusCode(respCode)
            .withContentType(MediaType.APPLICATION_JSON)
        );
    }

    private void mockStockPriceResponse(String path, Ticker ticker, int respCode){
        //mock stock-service
        var respBody = resourceToString(path);
        var reqPath = String.format("/stock/%s", ticker.name());
        mockClient
        .when(HttpRequest.request(reqPath))
        .respond(
            HttpResponse.response(respBody)
            .withStatusCode(respCode)
            .withContentType(MediaType.APPLICATION_JSON)
        ); 
    }

    private WebTestClient.BodyContentSpec postTrade(TradeRequest tradeRequest, HttpStatus expStatus) {
        return client.post().uri("/customers/1/trade")
        .bodyValue(tradeRequest)
        .exchange()
        .expectStatus().isEqualTo(expStatus)
        .expectBody()
        .consumeWith(e -> log.info("response: {}", new String(
            Objects.requireNonNull(e.getResponseBody()))));
    }

}
