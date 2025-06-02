package jayslabs.microservices.aggregator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import jayslabs.microservices.aggregator.client.CustomerServiceClient;
import jayslabs.microservices.aggregator.client.StockServiceClient;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
public class ServiceClientsConfig {

    @Bean
    public CustomerServiceClient customerServiceClient(@Value("${customer-service.url}") String baseUrl) {
        return new CustomerServiceClient(buildWebClient(baseUrl));
    }

    @Bean
    public StockServiceClient stockServiceClient(@Value("${stock-service.url}") String baseUrl) {
        return new StockServiceClient(buildWebClient(baseUrl));
    }

    private WebClient buildWebClient(String baseUrl) {
        log.info("Building WebClient for baseUrl: {}", baseUrl);
        return WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }
} 