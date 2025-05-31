package jayslabs.microservices.aggregator.exceptions;

public class InvalidTradeRequestException extends RuntimeException {
    
    public InvalidTradeRequestException(String msg) {
        super(msg);
    }
} 