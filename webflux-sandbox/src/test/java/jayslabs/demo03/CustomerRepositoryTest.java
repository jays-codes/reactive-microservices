package jayslabs.demo03;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import jayslabs.demo03.entity.Customer;
import jayslabs.demo03.repository.CustomerRepository;
import reactor.test.StepVerifier;

@SpringBootTest(properties = {"logging.level.org.springframework.r2dbc=INFO"})
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

    //Assignment21: find all customers whose email ends with ke@gmail.com
    @Test
    public void findByEmailEndingWith() {
        this.custRepo.findByEmailEndingWith("ke@gmail.com")
        .doOnNext(c -> log.info("{}", c))
        .as(StepVerifier::create)
        //check if the email ends with ke@gmail.com
        .assertNext(c -> Assertions.assertTrue(c.getEmail().endsWith("ke@gmail.com")))
        .assertNext(c -> Assertions.assertTrue(c.getEmail().endsWith("ke@gmail.com")))
        .verifyComplete();
    }

    //test insert customer
    @Test
    public void insertAndDeleteCustomer() {
        var cust = new Customer();
        cust.setName("anya");
        cust.setEmail("anya@forger.com");
        
        this.custRepo.save(cust)
        .doOnNext(c -> {
            log.info("{}", c);
        })
        .as(StepVerifier::create)
        .assertNext(c -> Assertions.assertNotNull(c.getId()))
        .verifyComplete();

        //count
        this.custRepo.count()
        .as(StepVerifier::create)
        .expectNext(11L)
        .verifyComplete();

        //delete
        this.custRepo.deleteById(cust.getId())
        .then(this.custRepo.count())
        .as(StepVerifier::create)
        .expectNext(10L)
        .verifyComplete();

    }

    //test update customer
    @Test
    public void updateCustomerEmailByName() {
        //find the customer by name
        this.custRepo.findByName("ethan")
        .doOnNext(c -> {
            log.info("{}", c);
            c.setEmail("anya@forger.com");
        })
        .flatMap(this.custRepo::save)
        .doOnNext(c -> log.info("{}", c))
        .as(StepVerifier::create)
        .assertNext(c -> Assertions.assertEquals("anya@forger.com", c.getEmail()))
        .verifyComplete();

        //update the email

    }
}
