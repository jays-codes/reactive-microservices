package jayslabs.microservices.customers.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import jayslabs.microservices.customers.domain.Ticker;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("portfolio_item")
public class PortfolioItem {
    @Id
    private Integer id;
    private Integer customerId;
    private Ticker ticker;
    private Integer quantity;
} 