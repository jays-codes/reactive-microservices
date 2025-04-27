package jayslabs.section8.exceptions;

import reactor.core.publisher.Mono;

public class ApplicationExceptions {

    public static <T> Mono<T> customerNotFound(Integer id){
        return Mono.error(new CustomerNotFoundException(id));
    }

    public static <T> Mono<T> missingName(){
        return Mono.error(new InvalidInputException("Name is required"));
    }

    public static <T> Mono<T> missingEmail(){
        return Mono.error(new InvalidInputException("Valid Email is required"));
    }

}
    