package jayslabs.microservices.customers.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jayslabs.microservices.customers.dto.CustomerInfoDTO;
import jayslabs.microservices.customers.dto.StockTradeRequest;
import jayslabs.microservices.customers.dto.StockTradeResponse;
import jayslabs.microservices.customers.service.CustomerService;
import jayslabs.microservices.customers.service.TradeService;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("customers")
@RequiredArgsConstructor
public class CustomerController {
    
    private final CustomerService customerService;
    private final TradeService tradeService;
    
    @GetMapping("/{id}")
    public Mono<CustomerInfoDTO> getCustomerInfo(@PathVariable Integer id) {
        return customerService.findCustomerInfoById(id);
    }
    
    @PostMapping("/{id}/trade")
    public Mono<StockTradeResponse> processTrade(@PathVariable Integer id, 
                                                 @RequestBody Mono<StockTradeRequest> requestMono) {
        return requestMono.flatMap(request -> tradeService.processTrade(id, request));
    }
} 