package jayslabs.section8.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.RequestPredicates;
import jayslabs.section8.exceptions.InvalidInputException;
import jayslabs.section8.exceptions.CustomerNotFoundException;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicate;
import java.util.function.BiFunction;
@Configuration
public class Assignment92CalculatorRouter {

    // ------------------------------------------------------------
    // version 2
    // +, -, *, / from header
    // a and b from path
    // if a or b is 0, return 400 bad request
    // if operation is not +, -, *, /, return 400 bad request "operation header should be of +, -, *, /"
    // else return result of operation

    @Bean
    public RouterFunction<ServerResponse> calculatorRoutes(){
        return RouterFunctions.route()
        .path("calculator", this::routeDefinitions)
        .build();
    }

    private RouterFunction<ServerResponse> routeDefinitions(){
        return RouterFunctions.route()
        .GET("/{a}/0", badRequest("b cannot be 0"))
        .GET("/{a}/{b}", isOperation("+"), calculate((a, b) -> a + b))
        .GET("/{a}/{b}", isOperation("-"), calculate((a, b) -> a - b))
        .GET("/{a}/{b}", isOperation("*"), calculate((a, b) -> a * b))
        .GET("/{a}/{b}", isOperation("/"), calculate((a, b) -> a / b))
        .GET("/{a}/{b}", badRequest("operation header should be of +, -, *, /"))
        .build();
    }

    private HandlerFunction<ServerResponse> calculate(BiFunction<Integer, Integer, Integer> operation){
        return req -> {
            int a = Integer.parseInt(req.pathVariable("a"));
            int b = Integer.parseInt(req.pathVariable("b"));
            return ServerResponse.ok().bodyValue(operation.apply(a, b));
        };
    }

    private RequestPredicate isOperation(String operation){
        return RequestPredicates.headers(headersObj -> 
            headersObj.header("operation").contains(operation));
    }
    private HandlerFunction<ServerResponse> badRequest(String message){
        return req -> ServerResponse.badRequest().bodyValue(message);
    }

    



    // ------------------------------------------------------------
    // version 1
    // private final CustomerRequestHandler handler;
    // private final ApplicationExceptionHandler exceptionHandler;
    
    // public RouterConfiguration(CustomerRequestHandler handler, 
    // ApplicationExceptionHandler exceptionHandler){
    //     this.handler = handler;
    //     this.exceptionHandler = exceptionHandler;
    // }

    // @Bean
    // public RouterFunction<ServerResponse> customerRoutes(){
    //     return RouterFunctions.route()
        
        
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
}