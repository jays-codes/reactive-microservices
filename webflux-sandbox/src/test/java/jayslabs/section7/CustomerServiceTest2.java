// package jayslabs.section7;

// import org.junit.jupiter.api.Test;
// import org.slf4j.Logger;
// import org.slf4j.LoggerFactory;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.http.HttpStatus;
// import org.springframework.test.web.reactive.server.WebTestClient;

// import jayslabs.section6.dto.CustomerDTO;

// @AutoConfigureWebTestClient
// @SpringBootTest(properties = "sec=section7")
// public class CustomerServiceTest2 {

//     @Autowired
//     private WebTestClient client;

//     // just validate HTTP response status codes!
//     // unauthorized - no token
//     // unauthorized - invalid token
//     // standard category - GET - success
//     // standard category - POST/PUT/DELETE - forbidden
//     // prime category - GET - success
//     // prime category - POST/PUT/DELETE - success

//     @Test
//     public void unauthorized(){
//         // no token
//         this.client.get()
//                    .uri("/customers")
//                    .exchange()
//                    .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
//         // invalid token
//         this.validateGet("secret", HttpStatus.UNAUTHORIZED);
//     }

//     @Test
//     public void standardCategory(){
//         this.validateGet("secret123", HttpStatus.OK);
//         this.validatePost("secret123", HttpStatus.FORBIDDEN);
//     }

//     @Test
//     public void primeCategory(){
//         this.validateGet("secret456", HttpStatus.OK);
//         this.validatePost("secret456", HttpStatus.OK);
//     }

//     private void validateGet(String token, HttpStatus expectedStatus) {
//         this.client.get()
//                    .uri("/customers")
//                    .header("auth-token", token)
//                    .exchange()
//                    .expectStatus().isEqualTo(expectedStatus);
//     }

//     private void validatePost(String token, HttpStatus expectedStatus) {
//         var dto = new CustomerDTO(null, "Anya Forger", "anya.forger@ostania.com");
//         this.client.post()
//                    .uri("/customers")
//                    .bodyValue(dto)
//                    .header("auth-token", token)
//                    .exchange()
//                    .expectStatus().isEqualTo(expectedStatus);
//     }
// }
