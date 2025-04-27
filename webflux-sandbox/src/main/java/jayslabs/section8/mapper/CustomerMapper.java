package jayslabs.section8.mapper;

//import org.mapstruct.Mapper;

import jayslabs.section8.dto.CustomerDTO;
import jayslabs.section8.entity.Customer;

// @Mapper(componentModel = "spring")
// public interface CustomerMapperMS {
//     CustomerDTO toDTO(Customer customer);
//     Customer toEntity(CustomerDTO dto);
// }

public class CustomerMapper {

    public static Customer toEntity(CustomerDTO dto){
        var customer = new Customer();
        customer.setName(dto.name());
        customer.setEmail(dto.email());
        customer.setId(dto.id());
        return customer;
    }

    public static CustomerDTO toDTO(Customer customer){
        return new CustomerDTO(
                customer.getId(),
                customer.getName(),
                customer.getEmail()
        );
    }

}