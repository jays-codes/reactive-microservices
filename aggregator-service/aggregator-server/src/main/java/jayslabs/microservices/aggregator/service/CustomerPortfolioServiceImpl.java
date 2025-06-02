package jayslabs.microservices.aggregator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jayslabs.microservices.aggregator.client.CustomerServiceClient;
import jayslabs.microservices.aggregator.client.StockServiceClient;
import jayslabs.microservices.aggregator.client.dto.CustomerInfoDTO;
import jayslabs.microservices.aggregator.client.dto.StockPriceResponse;
import jayslabs.microservices.aggregator.client.dto.StockTradeResponse;
import jayslabs.microservices.aggregator.client.dto.TradeRequest;
import jayslabs.microservices.aggregator.mapper.StockTradeMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CustomerPortfolioServiceImpl implements CustomerPortfolioService {

    private final Logger log = LoggerFactory.getLogger(CustomerPortfolioServiceImpl.class);

    private final CustomerServiceClient customerServiceClient;
    private final StockServiceClient stockServiceClient;

    @Override
    public Mono<CustomerInfoDTO> getCustomerPortfolio(Integer customerId) {
        return customerServiceClient.getCustomerInfo(customerId);
    }

    @Override
    public Mono<StockTradeResponse> processTrade(Integer custId, TradeRequest request) {
        return this.stockServiceClient.getStockPrice(request.ticker())
        .map(StockPriceResponse::price)
        .map(price -> StockTradeMapper.toStockTradeRequest(request, price))
        .flatMap(stockTradeRequest -> customerServiceClient.processTrade(custId, stockTradeRequest));
    }
}