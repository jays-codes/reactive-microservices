package jayslabs.microservices.aggregator;

import java.util.Objects;

import org.junit.jupiter.api.Test;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.MediaType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CustomerInformationTest extends AbstractIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerInformationTest.class);

    @Test
    public void testCustomerInformation() {

        //mock customer-service
        var respBody = resourceToString("customer-service/customer-information-200.json");
        
        mockClient
        .when(HttpRequest.request().withMethod("GET").withPath("/customers/2"))
        .respond(
            HttpResponse.response(respBody)
            .withStatusCode(200)
            .withContentType(MediaType.APPLICATION_JSON)
        );

        client.get().uri("/customers/1")
        .exchange()
        .expectBody()
        .consumeWith(e -> log.info("response: {}", new String(
            Objects.requireNonNull(e.getResponseBody()))));

    }
}
