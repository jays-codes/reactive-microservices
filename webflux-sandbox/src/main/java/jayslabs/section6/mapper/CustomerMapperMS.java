package jayslabs.section6.mapper;

import org.mapstruct.Mapper;

import jayslabs.section6.dto.CustomerDTO;
import jayslabs.section6.entity.Customer;

@Mapper(componentModel = "spring")
public interface CustomerMapperMS {
    CustomerDTO toDTO(Customer customer);
    Customer toEntity(CustomerDTO dto);
}
