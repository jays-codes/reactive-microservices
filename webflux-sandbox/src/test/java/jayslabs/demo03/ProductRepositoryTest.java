package jayslabs.demo03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

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

    @Test
    public void findPageableAllBy() {

        //get 1st page with 3 items per page, sorted by price ascending
        PageRequest pageRequest = PageRequest.of(
            0, 3, Sort.by("price").ascending());


        this.repo.findAllBy(pageRequest)
        .doOnNext(p -> log.info("{}", p))
        .as(StepVerifier::create)
        .assertNext(p -> Assertions.assertEquals(200, p.getPrice()))
        .assertNext(p -> Assertions.assertEquals(250, p.getPrice()))
        .assertNext(p -> Assertions.assertEquals(300, p.getPrice()))
        .verifyComplete();
    }
}
