package jayslabs.microservices.aggregator;

import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.mockserver.client.MockServerClient;
import org.mockserver.configuration.ConfigurationProperties;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@MockServerTest
@AutoConfigureWebTestClient
@SpringBootTest(properties = {
    "customer-service.url=http://localhost:${mockServerPort}",
    "stock-service.url=http://localhost:${mockServerPort}"
})
public abstract class AbstractIntegrationTest {

    protected MockServerClient mockClient;
    private static final Path TEST_RESOURCES_DIR = Path.of("src/test/resources");

    @Autowired
    protected WebTestClient client;

    @BeforeAll
    public static void setup() {
        ConfigurationProperties.disableLogging(true);
    }

    protected String resourceToString(String path) {
        try {
            return Files.readString(TEST_RESOURCES_DIR.resolve(path));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read resource: " + path, e);
        }    
    }
}
