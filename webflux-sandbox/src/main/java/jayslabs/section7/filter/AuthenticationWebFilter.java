package jayslabs.section7.filter;

import java.util.Map;
import java.util.Objects;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.core.annotation.Order;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

@Service
@Order(1)
public class AuthenticationWebFilter implements WebFilter {


    @Autowired
    private FilterErrorHandler filterErrorHandler;

    private static final Map<String, Category> TOKEN_CATEGORY_MAP = 
        Map.of(
        "secret123", Category.STANDARD,
        "secret456", Category.PRIME
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        var token = exchange.getRequest().getHeaders().getFirst("auth-token");
        if (
            Objects.nonNull(token) &&
            TOKEN_CATEGORY_MAP.containsKey(token)) {
            exchange.getAttributes().put("category", TOKEN_CATEGORY_MAP.get(token));
            return chain.filter(exchange);
        } else {            
            // return Mono.fromRunnable(
            //     () ->exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED));
            return filterErrorHandler
                .sendProblemDetail(
                    exchange, 
                    HttpStatus.UNAUTHORIZED, "Invalid token");
        }
    }
}
