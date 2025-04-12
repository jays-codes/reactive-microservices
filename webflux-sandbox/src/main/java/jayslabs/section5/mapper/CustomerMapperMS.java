package jayslabs.section5.mapper;

import org.mapstruct.Mapper;

import jayslabs.section5.dto.CustomerDTO;
import jayslabs.section5.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapperMS {
    CustomerDTO toDTO(Customer customer);
    Customer toEntity(CustomerDTO dto);
}
