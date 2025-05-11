package jayslabs.section9;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import jayslabs.section9.dto.CalculatorRespDTO;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class Section09ErrorHandlingTest extends AbstractWebClient{

    private static final Logger log = LoggerFactory.getLogger(Section09ErrorHandlingTest.class);

    private final WebClient client = createWebClient();

    @Test
    public void errorResponseTest(){

        this.client.get()
        .uri("/lec05/calculator/{a}/{b}", 10, 20)
        .header("operation", "@")
        .retrieve()
        .bodyToMono(CalculatorRespDTO.class)
        .doOnError(WebClientResponseException.class, 
        ex -> log.info("{}", ex.getResponseBodyAs(
            ProblemDetail.class)))
        .onErrorReturn(WebClientResponseException.InternalServerError.class, 
        new CalculatorRespDTO(0, 0, null, 0.0))
        .onErrorReturn(WebClientResponseException.BadRequest.class, 
        new CalculatorRespDTO(0, 0, null, -1.0))
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    } 

    
    @Test
    public void exchangeTest(){

        this.client.get()
        .uri("/lec05/calculator/{a}/{b}", 10, 20)
        .header("operation", "@")
        .exchangeToMono(this::decode)
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    } 

    private Mono<CalculatorRespDTO> decode(ClientResponse response){
        return Mono.just(response)
            .doOnNext(r -> log.info("{}", r.statusCode()))
            .flatMap(r -> r.statusCode().isError() 
                ? handleError(r)
                : r.bodyToMono(CalculatorRespDTO.class));
    }

    private Mono<CalculatorRespDTO> handleError(ClientResponse response) {
        return response.bodyToMono(ProblemDetail.class)
            .doOnNext(pd -> log.info("{}", pd))
            .then(Mono.empty());
    }
}
