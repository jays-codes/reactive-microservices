package jayslabs.demo03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import jayslabs.demo03.repository.CustomerRepository;
import reactor.test.StepVerifier;

public class CustomerRepositoryTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(CustomerRepositoryTest.class);

    @Autowired
    private CustomerRepository custRepo;
    
    @Test
    public void findAll() {
        this.custRepo.findAll()
        .doOnNext(c -> log.info("{}", c))
        .as(StepVerifier::create)
        .expectNextCount(10)
        .verifyComplete();
    }

    //findById(2)
    @Test
    public void findById() {
        this.custRepo.findById(2)
        .doOnNext(c -> log.info("{}", c))
        .as(StepVerifier::create)
        .assertNext(c -> Assertions.assertEquals("mike", c.getName()))
        .verifyComplete();
    }

    //findByName(sam)
    @Test
    public void findByName() {
        this.custRepo.findByName("sam")
        .doOnNext(c -> log.info("{}", c))
        .as(StepVerifier::create)
        .assertNext(c -> Assertions.assertEquals(1, c.getId()))
        .verifyComplete();
    }
}
