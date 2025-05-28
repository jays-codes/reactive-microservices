package jayslabs.microservices.customers.exceptions;

public class InsufficientBalanceException extends RuntimeException {
    private static final String MESSAGE = "Customer [id=%d] has insufficient balance";

    public InsufficientBalanceException(Integer customerId) {
        super(String.format(MESSAGE, customerId));
    }
} 