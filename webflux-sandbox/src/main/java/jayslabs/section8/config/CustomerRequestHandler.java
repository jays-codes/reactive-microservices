package jayslabs.section8.config;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import jayslabs.section8.dto.CustomerDTO;
import jayslabs.section8.service.ICustomerService;
import reactor.core.publisher.Mono;

@Service
public class CustomerRequestHandler {

    private ICustomerService srvc;

    public CustomerRequestHandler(ICustomerService srvc){
        this.srvc = srvc;
    }

    public Mono<ServerResponse> getAllCustomers(ServerRequest request){
        return srvc.getAllCustomers()
        .as(
            flux -> ServerResponse.ok().body(
                flux,
                CustomerDTO.class));
    }
}
