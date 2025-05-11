package jayslabs.section9;

import org.springframework.web.reactive.function.client.WebClient;
import org.junit.jupiter.api.Test;
import jayslabs.section9.dto.ProductDTO;
import java.time.Duration;

public class Section09MonoTest extends AbstractWebClient{

    private final WebClient client = createWebClient();

    @Test
    public void simpleGetTest() throws InterruptedException{
        this.client.get()
        .uri("/lec01/product/{id}", 1)
        .retrieve()
        .bodyToMono(ProductDTO.class)
        .doOnNext(print())
        .subscribe();

        Thread.sleep(Duration.ofSeconds(2));
    }
}
