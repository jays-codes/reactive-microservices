package jayslabs.section11.service;

import java.time.Duration;
import java.util.concurrent.ThreadLocalRandom;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import jayslabs.section11.dto.ProductDTO;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Service
public class DataSetupService implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSetupService.class);
    private final ProductService prodsrvc;

    //create 1000 products with random price, 1 prod every second
    @Override
    public void run(String... args) throws Exception {
        log.info("DataSetupService.run()");

        Flux.range(1,1000)
            .delayElements(Duration.ofSeconds(1))
            .map(i -> 
                new ProductDTO(
                    null, 
                    "Product " + i, 
                    ThreadLocalRandom.current().nextInt(1, 100)))
            .flatMap(dto -> this.prodsrvc.saveProduct(Mono.just(dto)))
            .subscribe();
    }
}