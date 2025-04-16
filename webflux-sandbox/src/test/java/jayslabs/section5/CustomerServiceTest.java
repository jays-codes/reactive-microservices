package jayslabs.section5;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import jayslabs.section5.dto.CustomerDTO;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=section5")
public class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient client;

    @Test
    public void getAllCustomers() {
        this.client.get().uri("/customers")
        .exchange()
        .expectStatus().isOk()
        .expectHeader().contentType(MediaType.APPLICATION_JSON)
        .expectBodyList(CustomerDTO.class)
        .value(list -> log.info("{}", list))
        .hasSize(10);
    }

    @Test
    public void getAllCustomersPaginated() {
        this.client.get().uri("/customers/paginated?page=1&size=3")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody()
        .jsonPath("$.length()").isEqualTo(3)
        .jsonPath("$[2].id").isEqualTo(3);

    }
}
