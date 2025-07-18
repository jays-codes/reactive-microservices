package jayslabs.section8;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

import jayslabs.section6.dto.CustomerDTO;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=section8")
public class CustomerServiceTest {

    private static final Logger log = LoggerFactory.getLogger(CustomerServiceTest.class);

    @Autowired
    private WebTestClient client;



    // validate HTTP response status codes

    // unauthorized - no token
    @Test
    public void getAllCustomersUnauthorized() {
        this.client.get().uri("/customers")
        .exchange()
        .expectStatus().isUnauthorized();
    }

    // unauthorized - invalid token
    @Test
    public void getAllCustomersUnauthorizedInvalidToken() {
        this.client.get().uri("/customers")
        .header("auth-token", "secret000")
        .exchange()
        .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    // standard category - GET - success
    @Test
    public void getAllCustomersStandardSuccess() {
        this.client.get().uri("/customers")
        .header("auth-token", "secret123")
        .exchange()
        .expectStatus().isOk();
    }

    // standard category - POST/PUT/DELETE - forbidden
    @Test
    public void saveCustomersStandardForbidden() {
        this.client.post().uri("/customers")
        .header("auth-token", "secret123")
        .bodyValue(new CustomerDTO(null, "Anya Forger", 
        "anya.forger@ostania.com"))
        .exchange()
        .expectStatus().isForbidden();
    }

    // prime category - GET - success
    @Test
    public void getAllCustomersPrimeSuccess() {
        this.client.get().uri("/customers")
        .header("auth-token", "secret456")
        .exchange()
        .expectStatus().isOk();
    }
    
    // prime category - POST/PUT/DELETE - success
    @Test
    public void postPutDeleteCustomersPrimeSuccess() {
        //save a customer
        CustomerDTO customer = new CustomerDTO(null, "Anya Forger", 
        "anya.forger@ostania.com");
        this.client.post().uri("/customers")
        .header("auth-token", "secret456")
        .bodyValue(customer)
        .exchange()
        .expectStatus().isOk();

        //update the customer
        CustomerDTO customer2 = new CustomerDTO(11, "Anya Forger Desmond", 
        "anya.desmond@ostania.com");
        this.client.put().uri("/customers/11")
        .header("auth-token", "secret456")
        .bodyValue(customer2)
        .exchange()
        .expectStatus().isOk();

        //delete the customer
        this.client.delete().uri("/customers/11")
        .header("auth-token", "secret456")
        .exchange()
        .expectStatus().isNoContent();
        
    }



    // @Test
    // public void getAllCustomers() {
    //     this.client.get().uri("/customers")
    //     .exchange()
    //     .expectStatus().isOk()
    //     .expectHeader().contentType(MediaType.APPLICATION_JSON)
    //     .expectBodyList(CustomerDTO.class)
    //     .value(list -> log.info("{}", list))
    //     .hasSize(10);
    // }

    // @Test
    // public void getAllCustomersPaginated() {
    //     this.client.get().uri("/customers/paginated?page=1&size=3")
    //     .exchange()
    //     .expectStatus().is2xxSuccessful()
    //     .expectBody()
    //     .jsonPath("$.length()").isEqualTo(3)
    //     .jsonPath("$[2].id").isEqualTo(3);
    // }

    // @Test
    // public void getCustomerById() {
    //     this.client.get().uri("/customers/1")
    //     .exchange()
    //     .expectStatus().isOk()
    //     .expectBody()
    //     .jsonPath("$.id").isEqualTo(1)
    //     .jsonPath("$.name").isEqualTo("sam");
    // }

    // @Test
    // public void createCustomer() {
    //     CustomerDTO custDTO = new CustomerDTO(
    //         null, "anya forger", "anya.forger@ostania.com");
    //     this.client.post().uri("/customers")
    //     .bodyValue(custDTO)
    //     .exchange()
    //     .expectStatus().is2xxSuccessful()
    //     .expectBody()
    //     .consumeWith(r -> log.info("{}", 
    //     new String(Objects.requireNonNull(r.getResponseBody()))))
    //     .jsonPath("$.id").isEqualTo(12)
    //     .jsonPath("$.name").isEqualTo("anya forger")
    //     .jsonPath("$.email").isEqualTo("anya.forger@ostania.com");
    // }

    // //create and delete customer
    // @Test
    // public void deleteCustomer() {
    //     //createCustomer();
    //     CustomerDTO custDTO = new CustomerDTO(
    //         null, "anya forger", "anya.forger@ostania.com");
    //     this.client.post().uri("/customers")
    //     .bodyValue(custDTO)
    //     .exchange()
    //     .expectStatus().is2xxSuccessful();

    //     this.client.delete().uri("/customers/11")
    //     .exchange()
    //     .expectStatus().is2xxSuccessful()
    //     .expectBody().isEmpty();
    // }

    // //update customer
    // @Test
    // public void updateCustomer() {
    //     CustomerDTO custDTO = new CustomerDTO(
    //         null, "anya forger", "anya.forger@ostania.com");
    //     this.client.put().uri("/customers/5")
    //     .bodyValue(custDTO)
    //     .exchange()
    //     .expectStatus().is2xxSuccessful()
    //     .expectBody()
    //     .consumeWith(r -> log.info("{}", 
    //     new String(Objects.requireNonNull(r.getResponseBody()))))
    //     .jsonPath("$.id").isEqualTo(5)
    //     .jsonPath("$.name").isEqualTo("anya forger")
    //     .jsonPath("$.email").isEqualTo("anya.forger@ostania.com");
    // }

    // //validate customer not found
    // @Test
    // public void validateCustomerNotFound() {
    //     this.client.get().uri("/customers/100")
    //     .exchange()
    //     .expectStatus().is4xxClientError()
    //     .expectBody()
    //     .jsonPath("$.detail").isEqualTo("Customer [id=100] not found");

    //     //delete
    //     this.client.delete().uri("/customers/100")
    //     .exchange()
    //     .expectStatus().is4xxClientError()
    //     .expectBody()
    //     .jsonPath("$.detail").isEqualTo("Customer [id=100] not found");

    //     //update
    //     this.client.put().uri("/customers/100")
    //     .bodyValue(new CustomerDTO(
    //         null, "anya forger", "anya.forger@ostania.com"))
    //     .exchange()
    //     .expectStatus().is4xxClientError()
    //     .expectBody()
    //     .jsonPath("$.detail").isEqualTo("Customer [id=100] not found"); 

        
    // }

    // //validate customer invalid input - missing name
    // @Test
    // public void validateCustomerInvalidInputMissingName() {
    //     this.client.post().uri("/customers")
    //     .bodyValue(new CustomerDTO(
    //         null, null, "anya.forger@ostania.com"))
    //     .exchange()
    //     .expectStatus().is4xxClientError()
    //     .expectBody()
    //     .jsonPath("$.detail").isEqualTo("Name is required");
    // }

    // //validate customer invalid input - missing email
    // @Test
    // public void validateCustomerInvalidInputMissingEmail() {
    //     this.client.post().uri("/customers")
    //     .bodyValue(new CustomerDTO(
    //         null, "anya forger", null))
    //     .exchange()
    //     .expectStatus().is4xxClientError()
    //     .expectBody()
    //     .jsonPath("$.detail").isEqualTo("Valid Email is required");
    // }
    
    
}
