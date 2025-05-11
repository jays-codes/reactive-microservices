package jayslabs.section9;

import org.springframework.web.reactive.function.client.WebClient;
import org.junit.jupiter.api.Test;
import jayslabs.section9.dto.ProductDTO;
import java.time.Duration;
import java.util.stream.IntStream;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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

    @Test
    public void concurrentRequestsTest() throws InterruptedException{
        Flux.range(1, 100)
            .flatMap(this::getProduct)
            .subscribe();

        Thread.sleep(Duration.ofSeconds(2));
    }

    private Mono<ProductDTO> getProduct(Integer id) {
        return this.client.get()
            .uri("/lec01/product/{id}", id)
            .retrieve()
            .bodyToMono(ProductDTO.class)
            .doOnNext(print());
    }

    // @Test
    // public void concurrentRequests() throws InterruptedException{
    //     for (int i = 1; i <= 100; i++){
    //         this.client.get()
    //         .uri("/lec01/product/{id}", i)
    //         .retrieve()
    //         .bodyToMono(ProductDTO.class)
    //         .doOnNext(print())
    //         .subscribe();
    //     }

    //     Thread.sleep(Duration.ofSeconds(2));
    // }

    @Test
    public void concurrentRequestsTestWithParallel() throws InterruptedException{
        Flux.range(1, 100)
            .parallel()
            .runOn(Schedulers.parallel())
            .flatMap(this::getProduct)
            .subscribe();

        Thread.sleep(Duration.ofSeconds(2));
    }
}
