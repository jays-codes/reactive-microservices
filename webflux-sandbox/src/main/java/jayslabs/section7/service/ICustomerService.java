package jayslabs.section7.service;

import jayslabs.section7.dto.CustomerDTO;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
public interface ICustomerService {
    Flux<CustomerDTO> getAllCustomers();

    Flux<CustomerDTO> getAllCustomers(Integer page, Integer size);

    Mono<CustomerDTO> getCustomerById(Integer id);

    Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> customerDTO);

    Mono<CustomerDTO> updateCustomer(Integer id, Mono<CustomerDTO> customerDTO);

    Mono<Boolean> deleteCustomer(Integer id);
}
