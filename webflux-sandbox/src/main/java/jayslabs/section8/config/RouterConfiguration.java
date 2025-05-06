package jayslabs.section8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunctions;
import jayslabs.section8.exceptions.CustomerNotFoundException;
import jayslabs.section8.exceptions.InvalidInputException;

@Configuration
public class RouterConfiguration {

    private final CustomerRequestHandler handler;
    private final ApplicationExceptionHandler exceptionHandler;
    
    public RouterConfiguration(CustomerRequestHandler handler, 
    ApplicationExceptionHandler exceptionHandler){
        this.handler = handler;
        this.exceptionHandler = exceptionHandler;
    }

    // @Bean
    // public RouterFunction<ServerResponse> customerRoutes(){
    //     return RouterFunctions.route()
    //     .GET("/customers", handler::getAllCustomers)
    //     .GET("/customers/paginated", handler::getAllCustomersPaginated)
    //     .GET("/customers/{id}", handler::getCustomerById)
    //     .POST("/customers", handler::saveCustomer)
    //     .PUT("/customers/{id}", handler::updateCustomer)
    //     .DELETE("/customers/{id}", handler::deleteCustomer)
    //     .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
    //     .onError(InvalidInputException.class, exceptionHandler::handleException)
    //     .build();
    // }

    @Bean
    public RouterFunction<ServerResponse> allRoutes(){
        return RouterFunctions.route()
        .path("/customers", this::customerRoutes)
        .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
        .onError(InvalidInputException.class, exceptionHandler::handleException)
        
        //filter
        // .filter((request,next)-> {
        //     return ServerResponse.badRequest().build();
        // })
        // .filter((request,next)-> {
        //     return next.handle(request);
        // })

        .build();
    }


    private RouterFunction<ServerResponse> customerRoutes(){
        return RouterFunctions.route()
        .GET("/paginated", handler::getAllCustomersPaginated)
        .GET("/{id}", handler::getCustomerById)
        .GET(handler::getAllCustomers)
        .POST(handler::saveCustomer)
        .PUT("/{id}", handler::updateCustomer)
        .DELETE("/{id}", handler::deleteCustomer)
        .build();
    }
}
