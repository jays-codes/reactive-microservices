package jayslabs.section5;

import java.util.Objects;

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

    @Test
    public void getCustomerById() {
        this.client.get().uri("/customers/1")
        .exchange()
        .expectStatus().isOk()
        .expectBody()
        .jsonPath("$.id").isEqualTo(1)
        .jsonPath("$.name").isEqualTo("sam");
    }

    @Test
    public void createCustomer() {
        CustomerDTO custDTO = new CustomerDTO(
            null, "anya forger", "anya.forger@ostania.com");
        this.client.post().uri("/customers")
        .bodyValue(custDTO)
        .exchange()
        .expectStatus().isCreated()
        .expectBody()
        .consumeWith(r -> log.info("{}", 
        new String(Objects.requireNonNull(r.getResponseBody()))))
        .jsonPath("$.id").isEqualTo(11)
        .jsonPath("$.name").isEqualTo("anya forger")
        .jsonPath("$.email").isEqualTo("anya.forger@ostania.com");
    }

    //create and delete customer
    @Test
    public void deleteCustomer() {
        createCustomer();
        this.client.delete().uri("/customers/11")
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody().isEmpty();
    }

    //update customer
    @Test
    public void updateCustomer() {
        CustomerDTO custDTO = new CustomerDTO(
            null, "anya forger", "anya.forger@ostania.com");
        this.client.put().uri("/customers/5")
        .bodyValue(custDTO)
        .exchange()
        .expectStatus().is2xxSuccessful()
        .expectBody()
        .consumeWith(r -> log.info("{}", 
        new String(Objects.requireNonNull(r.getResponseBody()))))
        .jsonPath("$.id").isEqualTo(5)
        .jsonPath("$.name").isEqualTo("anya forger")
        .jsonPath("$.email").isEqualTo("anya.forger@ostania.com");
    }

    //validate customer not found
    @Test
    public void validateCustomerNotFound() {
        this.client.get().uri("/customers/100")
        .exchange()
        .expectStatus().isNotFound();

        //delete
        this.client.delete().uri("/customers/100")
        .exchange()
        .expectStatus().isNotFound();

        //update
        this.client.put().uri("/customers/100")
        .bodyValue(new CustomerDTO(
            null, "anya forger", "anya.forger@ostania.com"))
        .exchange()
        .expectStatus().is4xxClientError();

        
    }
}
