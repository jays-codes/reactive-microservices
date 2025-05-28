package jayslabs.microservices.customers.advice;

import java.net.URI;
import java.util.function.Consumer;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jayslabs.microservices.customers.exceptions.CustomerNotFoundException;
import jayslabs.microservices.customers.exceptions.InsufficientBalanceException;
import jayslabs.microservices.customers.exceptions.InsufficientSharesException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException ex) {
        return build(HttpStatus.NOT_FOUND, ex, problem -> {
            problem.setType(URI.create("https://example.com/customer-not-found"));
            problem.setTitle("Customer not found");
        });
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ProblemDetail handleException(InsufficientBalanceException ex) {
        return build(HttpStatus.BAD_REQUEST, ex, problem -> {
            problem.setType(URI.create("https://example.com/insufficient-balance"));
            problem.setTitle("Insufficient balance");
        });
    }

    @ExceptionHandler(InsufficientSharesException.class)
    public ProblemDetail handleException(InsufficientSharesException ex) {
        return build(HttpStatus.BAD_REQUEST, ex, problem -> {
            problem.setType(URI.create("https://example.com/insufficient-shares"));
            problem.setTitle("Insufficient Shares");
        });
    }

    private ProblemDetail build(HttpStatus status, Exception ex, Consumer<ProblemDetail> consumer) {
        var problem = ProblemDetail.forStatusAndDetail(status, ex.getMessage());
        consumer.accept(problem);
        return problem;
    }
} 