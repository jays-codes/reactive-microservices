package jayslabs.section5.mapper;

import jayslabs.section5.dto.CustomerDTO;
import jayslabs.section5.entity.Customer;

public class CustomerMapperBare{
    public static CustomerDTO toDTO(Customer customer) {
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getEmail());
    }

    public static Customer toEntity(CustomerDTO dto) {
        return new Customer(dto.id(), dto.name(), dto.email());
    }
    
    
}
