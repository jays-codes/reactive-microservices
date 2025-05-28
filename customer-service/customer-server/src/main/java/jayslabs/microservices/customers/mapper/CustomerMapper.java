package jayslabs.microservices.customers.mapper;

import java.util.List;
import java.util.stream.Collectors;

import jayslabs.microservices.customers.client.dto.CustomerInfoDTO;
import jayslabs.microservices.customers.client.dto.HoldingDTO;
import jayslabs.microservices.common.domain.Ticker;
import jayslabs.microservices.customers.client.dto.StockTradeResponse;
import jayslabs.microservices.customers.client.dto.StockTradeRequest;
import jayslabs.microservices.customers.entity.Customer;
import jayslabs.microservices.customers.entity.PortfolioItem;

public class CustomerMapper {
    public static CustomerInfoDTO toCustomerInfoDTO(Customer customer, List<PortfolioItem> portfolioItems) {
        return new CustomerInfoDTO(
            customer.getId(),
            customer.getName(),
            customer.getBalance(),
            portfolioItems.stream()
            .map(pi -> new HoldingDTO(pi.getTicker(), pi.getQuantity()))
            .collect(Collectors.toList())
        );
    }

    

    public static StockTradeResponse toStockTradeResponse(Integer custId, Integer balance, StockTradeRequest request) {
        return new StockTradeResponse(
            custId,
            request.ticker(),
            request.price(),
            request.quantity(),
            request.action(),
            request.totalPrice(),
            balance
        );
    }

    public static PortfolioItem toPortfolioItem(Integer custId, Ticker ticker) {
        return new PortfolioItem(null, custId, ticker, 0);
    }
} 