package jayslabs.section9;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import jayslabs.section9.dto.CalculatorRespDTO;
import java.util.Map;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

public class Section09QueryParamTest extends AbstractWebClient {

    private final WebClient client = createWebClient();

    @Test
    void testURIBuilderVariables() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
    
        this.client.get()
        .uri(
            uriBuilder -> 
            uriBuilder.path(path).query(query).build(10, 20, "+"))
        .retrieve()
        .bodyToMono(CalculatorRespDTO.class)
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    
    }

    @Test
    void testURIBuilderMap() {
        var path = "/lec06/calculator";
        var query = "first={first}&second={second}&operation={operation}";
    
        var map = Map.of(
            "first", 10,
            "second", 25,
            "operation", "*"
        );

        this.client.get()
        .uri(
            uriBuilder -> 
            uriBuilder.path(path).query(query).build(map))
        .retrieve()
        .bodyToMono(CalculatorRespDTO.class)
        .doOnNext(print())
        .then()
        .as(StepVerifier::create)
        .verifyComplete();
    
    }    
}
