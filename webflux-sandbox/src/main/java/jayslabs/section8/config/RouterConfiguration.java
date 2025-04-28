package jayslabs.section8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunctions;

@Configuration
public class RouterConfiguration {

    private final CustomerRequestHandler handler;

    public RouterConfiguration(CustomerRequestHandler handler){
        this.handler = handler;
    }

    @Bean
    public RouterFunction<ServerResponse> customerRoutes(){
        return RouterFunctions.route()
        .GET("/customers", handler::getAllCustomers)
        .GET("/customers/{id}", handler::getCustomerById)
        .GET("/customers/paginated", handler::getAllCustomersPaginated)
        .POST("/customers", handler::saveCustomer)
        .PUT("/customers/{id}", handler::updateCustomer)
        .DELETE("/customers/{id}", handler::deleteCustomer)
        .build();
    }
}
