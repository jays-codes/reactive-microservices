package jayslabs.section6.validator;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

import jayslabs.section6.dto.CustomerDTO;
import jayslabs.section6.exceptions.ApplicationExceptions;
import reactor.core.publisher.Mono;

public class RequestValidator {

    public static UnaryOperator<Mono<CustomerDTO>> validate(){
        return mono -> mono.filter(hasName())
        .switchIfEmpty(ApplicationExceptions.missingName())
        .filter(hasValidEmail())
        .switchIfEmpty(ApplicationExceptions.missingEmail());
    }

    private static Predicate<CustomerDTO> hasName(){
        return dto -> Objects.nonNull(dto.name());
    }

    private static Predicate<CustomerDTO> hasValidEmail(){
        return dto -> Objects.nonNull(dto.email()) && dto.email().contains("@");
    }
    
    
}
