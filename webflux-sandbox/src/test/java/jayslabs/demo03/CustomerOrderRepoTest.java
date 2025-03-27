package jayslabs.demo03;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jayslabs.demo03.repository.CustomerOrderRepository;
import reactor.test.StepVerifier;

@SpringBootTest(properties = {"logging.level.org.springframework.r2dbc=DEBUG"})
public class CustomerOrderRepoTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(CustomerOrderRepoTest.class);

    @Autowired
    private CustomerOrderRepository repo;

    @Test
    public void getProductsOrderedByCustomerName() {
        this.repo.getProductsOrderedByCustomerName("mike")
        .doOnNext(p -> log.info("{}", p))
        .as(StepVerifier::create)
        .expectNextCount(2)
        .verifyComplete();
    }

    @Test
    public void getCustomerOrdersByProductDescription() {
        this.repo.getCustomerOrdersByProductDescription("mac pro")
        .doOnNext(p -> log.info("{}", p))
        .as(StepVerifier::create)
        .expectNextCount(1)
        .verifyComplete();
    }
}
