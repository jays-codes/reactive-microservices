package jayslabs.section6.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import jayslabs.section6.dto.CustomerDTO;
import jayslabs.section6.mapper.CustomerMapper;
import jayslabs.section6.repository.CustomerRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CustomerServiceImpl implements ICustomerService {

    private final CustomerRepository repository;
    private final CustomerMapper mapper;

    public CustomerServiceImpl(CustomerRepository repository, CustomerMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Flux<CustomerDTO> getAllCustomers(Integer page, Integer size) {
        return 
        //this.repository.findBy(Pageable.ofSize(size).withPage(page))
        this.repository.findBy(PageRequest.of(page - 1, size))
            .map(this.mapper::toDTO);
    }

    @Override
    public Flux<CustomerDTO> getAllCustomers() {
        return this.repository.findAll()
            .map(this.mapper::toDTO);
    }

    @Override
    public Mono<CustomerDTO> getCustomerById(Integer id) {
        return this.repository.findById(id)
            .map(this.mapper::toDTO);
    }

    @Override
    public Mono<CustomerDTO> saveCustomer(Mono<CustomerDTO> mono) {
        return mono.map(this.mapper::toEntity)
            .flatMap(this.repository::save)
            .map(this.mapper::toDTO);
    }

    @Override
    public Mono<CustomerDTO> updateCustomer(Integer id, Mono<CustomerDTO> mono) {
        // option 3:
        return repository.findById(id)
            .flatMap(entity -> mono)
            .map(mapper::toEntity)
            .doOnNext(e -> e.setId(id))
            .flatMap(repository::save)
            .map(mapper::toDTO);
        
        // option 1:
        // return mono.map(this.mapper::toEntity)
        //     .flatMap(e -> this.repository.findById(id)
        //         .map(this.mapper::toDTO)
        //         .flatMap(c -> this.repository.save(e))
        //         .map(this.mapper::toDTO));

        // option 2:    
        // return this.repository.findById(id)
        //     .flatMap(c -> mono.map(this.mapper::toEntity)
        //         .doOnNext(e -> e.setId(c.getId()))
        //         .flatMap(this.repository::save)
        //         .map(this.mapper::toDTO));
        }

    @Override
    public Mono<Boolean> deleteCustomer(Integer id) {
        return this.repository.removeById(id);
    }
}
