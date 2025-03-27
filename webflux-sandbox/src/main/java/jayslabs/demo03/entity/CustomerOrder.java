package jayslabs.demo03.entity;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Table("customer_order")
public class CustomerOrder {

    @Id
    private UUID orderId;
    private Integer customerId;
    private Integer productId;
    private Integer amount;
    private Instant orderDate;
}
