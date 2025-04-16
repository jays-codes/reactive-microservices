package jayslabs.section5.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jayslabs.section5.dto.CustomerDTO;
import jayslabs.section5.service.ICustomerService;
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

    // @GetMapping("/{id}")
    // public Mono<CustomerDTO> getCustomerById(@PathVariable Integer id) {
    //     return service.getCustomerById(id);
    // }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> getCustomerById(@PathVariable Integer id) {
        
        return service.getCustomerById(id)
            .map(ResponseEntity::ok) // .map(dto -> ResponseEntity.ok(dto))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // @PostMapping
    // public Mono<CustomerDTO> saveCustomer(@RequestBody CustomerDTO customerDTO) {
    //     return service.saveCustomer(Mono.just(customerDTO));
    // }

    @PostMapping
    public Mono<ResponseEntity<CustomerDTO>> saveCustomer(@RequestBody Mono<CustomerDTO> monoCustDTO) {
        return service.saveCustomer(monoCustDTO)
            .map(dto -> ResponseEntity.created(URI.create("/customers/" + dto.id())).body(dto))
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // @PutMapping("/{id}")
    // public Mono<CustomerDTO> updateCustomer(@PathVariable Integer id, @RequestBody CustomerDTO customerDTO) {
    //     return service.updateCustomer(id, Mono.just(customerDTO));
    // }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<CustomerDTO>> updateCustomer(@PathVariable Integer id, @RequestBody Mono<CustomerDTO> monoCustDTO) {
        return service.updateCustomer(id, monoCustDTO)
            .map(ResponseEntity::ok)
            .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Integer id) {
        // return service.deleteCustomer(id)
        //     .map(deleted -> deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build());

        return service.deleteCustomer(id)
            .filter(deleted -> deleted)
            //.map(deleted -> ResponseEntity.ok().<Void>build())
            .map(deleted -> ResponseEntity.noContent().<Void>build())
            .defaultIfEmpty(ResponseEntity.notFound().build());

    }
}
