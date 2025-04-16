package jayslabs.section6.exceptions;



public class CustomerNotFoundException extends RuntimeException {

private static final String MESSAGE = "Customer [id=%s] not found";

    public CustomerNotFoundException(Integer id) {
        super(String.format(MESSAGE, id));
    }

}
