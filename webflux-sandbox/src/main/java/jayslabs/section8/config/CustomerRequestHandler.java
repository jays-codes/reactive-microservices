package jayslabs.section8.config;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import jayslabs.section8.dto.CustomerDTO;
import jayslabs.section8.exceptions.ApplicationExceptions;
import jayslabs.section8.service.ICustomerService;
import jayslabs.section8.validator.RequestValidator;
import reactor.core.publisher.Mono;

@Service
public class CustomerRequestHandler {

    private final ICustomerService service;

    public CustomerRequestHandler(ICustomerService service){
        this.service = service;
    }

    public Mono<ServerResponse> getAllCustomers(ServerRequest request){
        return service.getAllCustomers()
        .as(
            flux -> ServerResponse.ok().body(
                flux,
                CustomerDTO.class));
    }

    //paginated version of getAllCustomers
    public Mono<ServerResponse> getAllCustomersPaginated(ServerRequest request){
        var page = request.queryParam("page")
        .map(Integer::parseInt)
        .orElse(1);
        var size = request.queryParam("size")
        .map(Integer::parseInt)
        .orElse(3);
        
        return service.getAllCustomers(page, size)
        .as(
            flux -> ServerResponse.ok().body(flux, CustomerDTO.class));
    }
    
    //get customer by id
    public Mono<ServerResponse> getCustomerById(ServerRequest request){
        var id = Integer.parseInt(request.pathVariable("id"));
        return service.getCustomerById(id)
        .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
        .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> saveCustomer(ServerRequest request){
        return request.bodyToMono(CustomerDTO.class)
        .transform(RequestValidator.validate())
        .as(service::saveCustomer)
        .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> updateCustomer(ServerRequest request){
        var id = Integer.parseInt(request.pathVariable("id"));
        
        return request.bodyToMono(CustomerDTO.class)
        .transform(RequestValidator.validate())
        .as(validReq -> service.updateCustomer(id, validReq))
        .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
        .flatMap(ServerResponse.ok()::bodyValue);
    }

    public Mono<ServerResponse> deleteCustomer(ServerRequest request){
        var id = Integer.parseInt(request.pathVariable("id"));
        return service.deleteCustomer(id)
        .filter(deleted -> deleted)
        .switchIfEmpty(ApplicationExceptions.customerNotFound(id))
        .then(ServerResponse.noContent().build());
    }
}
