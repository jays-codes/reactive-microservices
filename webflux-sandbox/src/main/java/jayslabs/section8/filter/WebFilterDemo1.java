package jayslabs.section8.filter;

import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;

import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class WebFilterDemo1 implements WebFilter {

    private static final Logger log = LoggerFactory.getLogger(WebFilterDemo1.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        log.info("WebFilterDemo1 filter() called");
        return chain.filter(exchange);
    }
}
