package jayslabs.demo03.dto;

import java.time.Instant;
import java.util.UUID;

public record OrderDetailsDTO(
    UUID orderId,
    String customerName,
    String productName,
    Integer amount,
    Instant orderDate
) {

}
