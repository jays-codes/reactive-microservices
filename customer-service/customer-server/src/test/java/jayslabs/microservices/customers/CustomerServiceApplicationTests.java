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

import jayslabs.microservices.common.domain.Ticker;
import jayslabs.microservices.common.domain.TradeAction;
import jayslabs.microservices.customers.client.dto.StockTradeRequest;

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

	@Test
	public void testBuyAndSell(){
		processTrade(1, new StockTradeRequest(Ticker.APPLE, 100, 50, TradeAction.BUY), HttpStatus.OK)
		.jsonPath("$.ticker").isEqualTo("APPLE")
		.jsonPath("$.quantity").isEqualTo(50);

		processTrade(1, new StockTradeRequest(Ticker.APPLE, 50, 25, TradeAction.SELL), HttpStatus.OK)
		.jsonPath("$.ticker").isEqualTo("APPLE")
		.jsonPath("$.quantity").isEqualTo(25);

		getCustomer(1, HttpStatus.OK)
		.jsonPath("$.name").isEqualTo("Anya")
		.jsonPath("$.balance").isEqualTo(6250);

		processTrade(1, new StockTradeRequest(Ticker.GOOGLE, 25, 12, TradeAction.BUY), HttpStatus.OK)
		.jsonPath("$.ticker").isEqualTo("GOOGLE")
		.jsonPath("$.quantity").isEqualTo(12);

		getCustomer(1, HttpStatus.OK)
		.jsonPath("$.holdings").isNotEmpty()
		.jsonPath("$.holdings[0].ticker").isEqualTo("APPLE")
		.jsonPath("$.holdings[0].quantity").isEqualTo(25)
		.jsonPath("$.holdings[1].ticker").isEqualTo("GOOGLE")
		.jsonPath("$.holdings[1].quantity").isEqualTo(12);
	}


	@Test
	public void testCustomerNotFound(){
		getCustomer(100, HttpStatus.NOT_FOUND)
		.jsonPath("$.detail").isEqualTo("Customer [id=100] not found");
	}

	@Test
	public void testInsufficientShares(){
		processTrade(1, new StockTradeRequest(Ticker.AMAZON, 100, 50, TradeAction.SELL), HttpStatus.BAD_REQUEST)
		.jsonPath("$.detail").isEqualTo("Customer [id=1] has insufficient shares");
		
	}

	@Test
	public void testInsufficientFunds(){
		processTrade(1, new StockTradeRequest(Ticker.APPLE, 100000, 5, TradeAction.BUY), HttpStatus.BAD_REQUEST)
		.jsonPath("$.detail").isEqualTo("Customer [id=1] has insufficient balance");
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
			log.info("Trade Info: {}", new String(Objects.requireNonNull(result.getResponseBody())))
		);
	}

}
