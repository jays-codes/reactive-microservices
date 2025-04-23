package jayslabs.section6.advice;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import jayslabs.section6.exceptions.CustomerNotFoundException;
import jayslabs.section6.exceptions.InvalidInputException;
import org.springframework.http.ProblemDetail;
import org.springframework.http.HttpStatus;
import java.net.URI;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(CustomerNotFoundException.class)
    public ProblemDetail handleException(CustomerNotFoundException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setType(URI.create("https://example.com/not-found"));
        problem.setTitle("Customer not found");
        //problem.setDetail("The customer you are looking for does not exist");
        return problem;
    }

    @ExceptionHandler(InvalidInputException.class)
    public ProblemDetail handleException(InvalidInputException ex){
        var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, ex.getMessage());
        problem.setType(URI.create("https://example.com/invalid-input"));
        problem.setTitle("Invalid input");
        //problem.setDetail("The input you provided is invalid");
        return problem;
    }
}
