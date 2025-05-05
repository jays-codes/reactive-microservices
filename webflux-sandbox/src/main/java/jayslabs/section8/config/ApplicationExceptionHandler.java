package jayslabs.section8.config;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import jayslabs.section8.exceptions.CustomerNotFoundException;
import jayslabs.section8.exceptions.InvalidInputException;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import java.util.function.Consumer;
@Service
public class ApplicationExceptionHandler {

    public Mono<ServerResponse> handleException(CustomerNotFoundException ex, 
    ServerRequest request){
        return handleException(HttpStatus.NOT_FOUND, ex, request, problem -> {
            problem.setType(URI.create("https://example.com/not-found"));
            problem.setTitle("Customer not found");
        });
    }

    public Mono<ServerResponse> handleException(InvalidInputException ex, 
    ServerRequest request){
        return handleException(HttpStatus.BAD_REQUEST, ex, request, problem -> {
            problem.setType(URI.create("https://example.com/invalid-input"));
            problem.setTitle("Invalid input");
        });
    }

    private Mono<ServerResponse> handleException(HttpStatus status, Exception ex, 
    ServerRequest request, Consumer<ProblemDetail> consumer){
        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        problem.setInstance(URI.create(request.path()));
        consumer.accept(problem);
        return ServerResponse.status(status).bodyValue(problem);
    }
}
