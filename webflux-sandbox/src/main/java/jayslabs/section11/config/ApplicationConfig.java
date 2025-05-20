package jayslabs.section11.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import jayslabs.section11.dto.ProductDTO;
import reactor.core.publisher.Sinks;

@Configuration
public class ApplicationConfig {

    @Bean
    public Sinks.Many<ProductDTO> productSink() {
        return Sinks.many().replay().limit(1);
    }

}
