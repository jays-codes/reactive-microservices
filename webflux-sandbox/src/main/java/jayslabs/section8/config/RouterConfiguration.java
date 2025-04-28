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
        .GET("/customers", request -> {
            return handler.getAllCustomers(request);
        })
        
        .build();
    }
}
