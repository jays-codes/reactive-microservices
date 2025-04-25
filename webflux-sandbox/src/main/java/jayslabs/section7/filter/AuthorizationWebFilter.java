package jayslabs.section7.filter;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import reactor.core.publisher.Mono;
import jayslabs.section7.filter.FilterErrorHandler;
import org.springframework.beans.factory.annotation.Autowired;

@Service
@Order(2)
public class AuthorizationWebFilter implements WebFilter {

    @Autowired
    private FilterErrorHandler filterErrorHandler;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var category = exchange.getAttributeOrDefault("category", Category.STANDARD);

        return switch (category) {
            case STANDARD -> standard(exchange, chain);
            case PRIME -> prime(exchange, chain);
        };
    }

    private Mono<Void> prime(ServerWebExchange exchange, WebFilterChain chain) {
        return chain.filter(exchange);
    }

    private Mono<Void> standard(ServerWebExchange exchange, WebFilterChain chain) {
        
        var isGet = exchange.getRequest().getMethod() == HttpMethod.GET;
        return isGet ? chain.filter(exchange) : 
        // Mono.fromRunnable(
        //     () -> exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN));

        filterErrorHandler
        .sendProblemDetail(
            exchange, 
            HttpStatus.FORBIDDEN, "Standard users can only perform GET operations");

    }
}
