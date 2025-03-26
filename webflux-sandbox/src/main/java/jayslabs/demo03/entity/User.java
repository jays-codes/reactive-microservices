package jayslabs.demo03.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table("customer")
public class User {

    @Id
    private Integer id;
    private String name;
    private String email;
    
    
    
    
}
