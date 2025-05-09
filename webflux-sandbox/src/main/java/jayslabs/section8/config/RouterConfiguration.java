package jayslabs.section8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.RequestPredicates;
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
    //     .GET("/customers/{id}", RequestPredicates.path("*/1?"), handler::getCustomerById)
    //     .POST("/customers", handler::saveCustomer)
    //     .PUT("/customers/{id}", handler::updateCustomer)
    //     .DELETE("/customers/{id}", handler::deleteCustomer)
    //     .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
    //     .onError(InvalidInputException.class, exceptionHandler::handleException)
        
    //     //assignment
    //     //header "operation" is present and is of "+", "-", "*", "/"
    //     // and a and b are present in the path and are not 0
    //     // else return 400 bad request
    //     .GET("/calculator/{a}/{b}", 
    //     RequestPredicates.headers(headersObj -> 
    //         headersObj.header("operation").contains("+") || 
    //         headersObj.header("operation").contains("-") || 
    //         headersObj.header("operation").contains("*") || 
    //         headersObj.header("operation").contains("/")),
    //         (request) -> {
    //             try {
    //                 int a = Integer.parseInt(request.pathVariable("a"));
    //                 int b = Integer.parseInt(request.pathVariable("b"));
    //                 if (a != 0 && b != 0) {
    //                     return ServerResponse.ok().bodyValue(
    //                         request.pathVariable("a") + request.pathVariable("b")
    //                     );
    //                 } else {
    //                     return ServerResponse.badRequest().build();
    //                 }
    //             } catch (NumberFormatException e) {
    //                 return ServerResponse.badRequest().bodyValue("operation header should be of +, -, *, /");
    //             }
    //         })

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
        .GET("/{id}", RequestPredicates.path("*/1?"), handler::getCustomerById)
        .GET(handler::getAllCustomers)
        .POST(handler::saveCustomer)
        .PUT("/{id}", handler::updateCustomer)
        .DELETE("/{id}", handler::deleteCustomer)
        .build();
    }
}
