package jayslabs.demo03;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.r2dbc.core.DatabaseClient;

import jayslabs.demo03.dto.OrderDetailsDTO;
import reactor.test.StepVerifier;

@SpringBootTest(properties = {"logging.level.org.springframework.r2dbc=DEBUG"})
public class R2DBCDBClientTest extends AbstractTest {
    private static final Logger log = LoggerFactory.getLogger(R2DBCDBClientTest.class);

    @Autowired
    private DatabaseClient dbClient;

    @Test
    public void getOrderDetailsByProductDescription() {

        var query = this.dbClient.sql("""
                SELECT 
            co.order_id,
            c.name AS customer_name,
            p.description AS product_name,
            co.amount,
            co.order_date
        FROM
            customer c
            INNER JOIN customer_order co ON c.id = co.customer_id
            INNER JOIN product p ON p.id = co.product_id
        WHERE
            p.description = :description
        ORDER BY co.amount DESC
                """);


        //test that there are two orders for iphone 18
        query.bind("description", "iphone 18")
        .mapProperties(OrderDetailsDTO.class)
        .all()
        .doOnNext(p -> log.info("Order details: {}", p))
        .as(StepVerifier::create)
        .expectNextCount(3)
        .verifyComplete();
    
    }
}
