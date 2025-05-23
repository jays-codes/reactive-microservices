package jayslabs.section12;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

abstract class AbstractWebClient {

    private static final Logger log = LoggerFactory.getLogger(AbstractWebClient.class);

    protected <T> Consumer<T> print(){
        return item -> log.info("received: {}", item);
    }

    protected WebClient createWebClient(Consumer<WebClient.Builder> consumer){
        var builder = WebClient.builder()
        .baseUrl("http://localhost:7070/demo03");
        consumer.accept(builder);
        return builder.build();
    }

    protected WebClient createWebClient(){
        return createWebClient(b -> {});
    }

}
