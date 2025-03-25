package jayslabs.demo03;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import org.springframework.http.client.JdkClientHttpRequestFactory;

import reactor.core.publisher.Flux;

@RestController
@RequestMapping("traditional")
public class TraditionalProductController {

    private static final Logger log = LoggerFactory.getLogger(TraditionalProductController.class);
    
    private final RestClient restClient = RestClient.builder()
    .requestFactory(new JdkClientHttpRequestFactory())
    .baseUrl("http://localhost:7070")
    .build();

    @GetMapping("/products")
    public List<Product> getAllProducts() {

        var products = this.restClient.get()
        .uri("/demo01/products/notorious")
        .retrieve()
        .body(new ParameterizedTypeReference<List<Product>>() {});

        log.info("received response -> products: {}", products);
        return products;
    }

    // Flux doesn't mean this method is reactive. 
    // Products are still being retrieved in a blocking way and converted to  flux at the end
    @GetMapping("/products2")
    public Flux<Product> getAllProducts2() {

        var products = this.restClient.get()
        .uri("/demo01/products")
        .retrieve()
        .body(new ParameterizedTypeReference<List<Product>>() {});

        log.info("received response -> products: {}", products);
        return Flux.fromIterable(products);
    }

}
