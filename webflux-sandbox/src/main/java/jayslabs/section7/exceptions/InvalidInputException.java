package jayslabs.section7.exceptions;

public class InvalidInputException extends RuntimeException {

    //private static final String MESSAGE = "Invalid input [field=%s, value=%s]";

    public InvalidInputException(String message) {
        super(message);
    }

}
