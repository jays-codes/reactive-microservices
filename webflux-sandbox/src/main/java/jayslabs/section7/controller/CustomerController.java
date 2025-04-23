package jayslabs.section7.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jayslabs.section7.dto.CustomerDTO;
import jayslabs.section7.exceptions.ApplicationExceptions;
import jayslabs.section7.service.ICustomerService;
import jayslabs.section7.validator.RequestValidator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    private final ICustomerService service;

    public CustomerController(ICustomerService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<CustomerDTO> getAllCustomers() {
        return service.getAllCustomers();
    }

    //use RequestParam to get page and size
    @GetMapping("paginated")
    public Flux<CustomerDTO> getAllCustomers(
        @RequestParam(defaultValue = "1") Integer page, 
        @RequestParam(defaultValue = "3") Integer size) {
            return service.getAllCustomers(page, size);
    }

    @GetMapping("/{id}")
    public Mono<CustomerDTO> getCustomerById(@PathVariable Integer id) {
        
        return this.service.getCustomerById(id)
        .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }

    @PostMapping
    public Mono<CustomerDTO> saveCustomer(@RequestBody Mono<CustomerDTO> monoCustDTO) {
        return monoCustDTO
            .transform(RequestValidator.validate())
            .as(service::saveCustomer);
    }

    @PutMapping("/{id}")
    public Mono<CustomerDTO> updateCustomer(@PathVariable Integer id, @RequestBody Mono<CustomerDTO> monoCustDTO) {

        return monoCustDTO
            .transform(RequestValidator.validate())
            .as(validReq -> this.service.updateCustomer(id, validReq))
            .switchIfEmpty(ApplicationExceptions.customerNotFound(id));
    }


    @DeleteMapping("/{id}")
    public Mono<Void> deleteCustomer(@PathVariable Integer id) {

        return service.deleteCustomer(id)
            .filter(deleted -> deleted)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
            .then();

    }
}
