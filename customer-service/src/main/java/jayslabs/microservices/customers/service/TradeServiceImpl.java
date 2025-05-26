package jayslabs.microservices.customers.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jayslabs.microservices.customers.dto.StockTradeRequest;
import jayslabs.microservices.customers.dto.StockTradeResponse;
import jayslabs.microservices.customers.entity.Customer;
import jayslabs.microservices.customers.entity.PortfolioItem;
import jayslabs.microservices.customers.exceptions.ApplicationExceptions;  
import jayslabs.microservices.customers.mapper.CustomerMapper;
import jayslabs.microservices.customers.repository.CustomerRepository;
import jayslabs.microservices.customers.repository.PortfolioItemRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class TradeServiceImpl implements TradeService {
    
    private final CustomerRepository custrepo;  
    private final PortfolioItemRepository portfoliorepo;

    @Transactional
    @Override
    public Mono<StockTradeResponse> processTrade(Integer customerId, StockTradeRequest request) {
        return switch (request.action()) {
            case BUY -> this.buyStock(customerId, request);
            case SELL -> this.sellStock(customerId, request);
        };
    }

    private Mono<StockTradeResponse> buyStock(Integer custId, StockTradeRequest request) {
        var custMono = this.custrepo.findById(custId)
            .switchIfEmpty(ApplicationExceptions.customerNotFound(custId))
            .filter(cust -> cust.getBalance() >= request.totalPrice())
            .switchIfEmpty(ApplicationExceptions.insufficientBalance(custId));

        var portfolioItemMono = this.portfoliorepo.findByCustomerIdAndTicker(custId, request.ticker())
        .defaultIfEmpty(CustomerMapper.toPortfolioItem(custId, request.ticker()));        

        return custMono.zipWhen(customer -> portfolioItemMono)
        .flatMap(t -> executeBuy(t.getT1(), t.getT2(), request));

    }

    private Mono<StockTradeResponse> executeBuy(Customer cust, PortfolioItem pi, StockTradeRequest request) {
        cust.setBalance(cust.getBalance() - request.totalPrice());
        pi.setQuantity(pi.getQuantity() + request.quantity());
        
        var response = CustomerMapper.toStockTradeResponse(cust.getId(), cust.getBalance(), request);
        
        return Mono.zip(this.custrepo.save(cust), this.portfoliorepo.save(pi))
        .thenReturn(response);
        //.map(tuple -> response);
    }
    
    private Mono<StockTradeResponse> sellStock(Integer customerId, StockTradeRequest request) { 
        return Mono.empty();
        // return custsrvc.findCustomerInfoById(customerId)
        // .flatMap(cust -> {
        //     if (cust.balance() < request.price() * request.quantity()) {
        //         return Mono.error(new InsufficientBalanceException(customerId));
        //     }
        // }).map(cust -> CustomerMapper.toStockTradeResponse(cust, request));
    }
} 