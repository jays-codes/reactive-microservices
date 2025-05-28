package jayslabs.microservices.aggregator.exceptions;

import reactor.core.publisher.Mono;

import jayslabs.microservices.aggregator.exceptions.CustomerNotFoundException;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer id) {
        return Mono.error(new CustomerNotFoundException(id));
    }

} 