package jayslabs.microservices.aggregator;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

public class CustomerInformationTest extends AbstractIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerInformationTest.class);

    @Test
    public void testCustomerInformation() {

        //mock customer-service
        // given
        mockCustomerInformation("customer-service/customer-information-200.json", 200);

        // then
        getCustomerInformation(HttpStatus.OK)
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.name").isEqualTo("Anya Forger")
        .jsonPath("$.balance").isEqualTo(10000)
        .jsonPath("$.holdings[0].ticker").isEqualTo("APPLE")
        .jsonPath("$.holdings[0].quantity").isEqualTo(100)
        .jsonPath("$.holdings[1].ticker").isEqualTo("GOOGLE")
        .jsonPath("$.holdings[1].quantity").isEqualTo(200);

    }


    private void mockCustomerInformation(String path, int respCode){
        //mock customer-service
        var respBody = resourceToString(path);
        
        mockClient
        .when(HttpRequest.request().withMethod("GET").withPath("/customers/1"))
        .respond(
            HttpResponse.response(respBody)
            .withStatusCode(respCode)
            .withContentType(MediaType.APPLICATION_JSON)
        ); 
    }

    private WebTestClient.BodyContentSpec getCustomerInformation(HttpStatus expStatus) {
        return client.get().uri("/customers/1")
        .exchange()
        .expectStatus().isEqualTo(expStatus)
        .expectBody()
        .consumeWith(e -> log.info("response: {}", new String(
            Objects.requireNonNull(e.getResponseBody()))));
    }
}
