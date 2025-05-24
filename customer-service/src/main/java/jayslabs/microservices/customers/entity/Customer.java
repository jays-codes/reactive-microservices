package jayslabs.microservices.customers.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("customer")
public class Customer {
    @Id
    private Integer id;
    private String name;
    private Integer balance;
} 