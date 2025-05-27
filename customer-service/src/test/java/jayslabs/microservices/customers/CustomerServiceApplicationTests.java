package jayslabs.microservices.customers;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import jayslabs.microservices.customers.domain.Ticker;
import jayslabs.microservices.customers.domain.TradeAction;
import jayslabs.microservices.customers.dto.StockTradeRequest;

@SpringBootTest
@AutoConfigureWebTestClient
class CustomerServiceApplicationTests {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceApplicationTests.class);
	
	@Autowired
	private WebTestClient client;

	@Test
	public void testCustomerInformation(){
		getCustomer(1, HttpStatus.OK)
		.jsonPath("$.name").isEqualTo("Anya")
		.jsonPath("$.balance").isEqualTo(10000)
		.jsonPath("$.holdings").isEmpty();
	}

	@Test
	public void testCustomerTrade(){
		processTrade(1, new StockTradeRequest(Ticker.APPLE, 100, 50, TradeAction.BUY), HttpStatus.OK)
		.jsonPath("$.ticker").isEqualTo("APPLE")
		.jsonPath("$.quantity").isEqualTo(50);

		getCustomer(1, HttpStatus.OK)
		.jsonPath("$.name").isEqualTo("Anya")
		.jsonPath("$.balance").isEqualTo(5000)
		.jsonPath("$.holdings").isNotEmpty()
		.jsonPath("$.holdings[0].ticker").isEqualTo("APPLE")
		.jsonPath("$.holdings[0].quantity").isEqualTo(50);
	
	}

	private WebTestClient.BodyContentSpec getCustomer(Integer id, HttpStatus expectedStatus){
		return this.client.get().uri("/customers/{id}", id)
		.exchange()
		.expectStatus().isEqualTo(expectedStatus)
		.expectBody()
		.consumeWith(result -> 
			log.info("Customer Info: {}", new String(Objects.requireNonNull(result.getResponseBody()	)))
		);
	}

	private WebTestClient.BodyContentSpec processTrade(
		Integer id, StockTradeRequest request, HttpStatus expectedStatus){
		return this.client.post().uri("/customers/{id}/trade", id)
		.bodyValue(request)
		.exchange()
		.expectStatus().isEqualTo(expectedStatus)
		.expectBody()
		.consumeWith(result -> 
			log.info("Trade Info: {}", Objects.requireNonNull(result.getResponseBody()))
		);
	}

}
