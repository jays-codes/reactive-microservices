package jayslabs.demo03;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jayslabs.demo03.repository.ProductRepository;
import reactor.test.StepVerifier;

@SpringBootTest(properties = {"logging.level.org.springframework.r2dbc=DEBUG"})
public class ProductRepositoryTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(ProductRepositoryTest.class);

    @Autowired
    private ProductRepository repo;
    
    @Test
    public void findAll() {
        this.repo.findAll()
        .doOnNext(p -> log.info("{}", p))
        .as(StepVerifier::create)
        .expectNextCount(10)
        .verifyComplete();
    }

    @Test
    public void findByPriceBetween() {
        this.repo.findByPriceBetween(200, 1000)
        .doOnNext(p -> log.info("{}", p))
        .as(StepVerifier::create)
        .expectNextCount(7)
        .verifyComplete();
    }
}
