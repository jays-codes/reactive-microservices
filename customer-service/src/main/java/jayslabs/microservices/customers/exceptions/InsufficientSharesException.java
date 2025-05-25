package jayslabs.microservices.customers.exceptions;

public class InsufficientSharesException extends RuntimeException {
    private static final String MESSAGE = "Customer [id=%d] has insufficient shares";

    public InsufficientSharesException(Integer customerId) {
        super(String.format(MESSAGE, customerId));
    }
} 