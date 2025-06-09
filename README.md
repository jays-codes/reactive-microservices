# reactive-microservices
Jay's project/practice repo for Reactive Microservices : WebFlux + Project Reactor

proj: Stock Trading Services
- Important: refer to [BP]README2.md [added] for Stock Trading Microservices details
- Major Refactor of aggregator-service and customer-service to remove parent relationship with reactive-microservices: modified individual project pom and submodule poms
- [BP] Major Refactor of aggregator-service and customer-service to conform to DDD 
principles: separate microservice into server and client submodules; expose clean API 
contract, common-client module for shared domain logic, Bounded Context implementation

proj: aggregator-service
- refactored CIT: added mockCustomerInfo(path, respCode), getCustomerInfo(HttpStatus), testCustomerInfo() to call previous two methods
- created CustomerInformationTest (CIT): testCustomerInfo(). response data json file; modified AbstractIntegrationTest - added resourceToString()
- created AbstractIntegrationTest (aggregator-service/src/test/java): uses mock-server dependency. Uses @MockServerTest, @AutoConfigureWebTestClient, @SpringBootTest, MockServerClient, WebTestClient
- created ServiceClientsConfig (aggregator-server/config) for StockServiceClient and CustomerServiceClient; passes in values from app.yaml: customer-service.url, stock-service.url
- Added Exception handling via @ControllerAdvice, aggregator-server/advice.ApplicationExceptionHandler
- created CustomerPortfolioControoler, StockPriceStreamController, <<StockPriceService>> and Impl
- created <<CustomerPortfolioService>>, impl class (@Service) with getCustomperPortfolio():Mono<CustomerInfoDTO>, processTrade(custId, TradeReq):Mono<StockTradeResp> - which calls stockClient.getStockPrice(ticker), and custClient.processTrade(custId, StockTradeRequest); created StockTradeMapper class with .toStockTradeRequest(TradeRequest, price):StockTradeRequest  
- created CustomerServiceClient (pkg: aggregator-server/aggregator.client): reactive WebClient to communicate
with Customer-Service: getCustomerIno(custId):Mono<CustomerInfoDTO>, 
processTrade(custId, StockTradeRequest):Mono<StockTradeResponse>, handleException(BadRequest): Mono<T>
- StockServiceClient: Modified StockServiceClient into a Hot Publisher -> getPriceUpdates(); added cache(1) and retry logic: retryWhen(), 
retry(), Retry.fixedDelay(), doBeforeRetry(), RetrySignal.failure(); added attribute priceUpdateFlux : Flux<PriceUpdateDTO> and initialization 
method calling getPriceUpdates()
- created StockServiceClient(pkg: aggregator-server/aggregator.client): reactive WebClient to connect to Stock Service; 
getStockPrice(Ticker):Mono<StockPriceResponse>, getPriceUpdates():Flux<PriceUpdateDTO>
- [BP] Added in aggregator.exceptions package: InvalidTradeRequestException, ApplicationExceptions 
with factory methods returning Mono<T> where t is exception type; RequestValidator with HOFs, validate(), returning 
UnaryOperator<Mono<TradeRequest>> or Mono<Exception> via use of AppExceptions factory methods. 
for validation error related to TradeRequest
- create packages: advice, client, config, controller, domain, dto, exceptions, service, validator; application.yaml, logback.xml, data.sql
- initial proj create, SpringBoot 3.5.0, jdk 21, jar; dep: Spring Reactive Web, Lombok

proj: customer-service
- CustomerServiceApplicationTests: additional Tests: buyAndSell(), negative tests: customerNotFound(), insufficientShares(), insufficientBalance(); used .jsonPath("$.detail") to evaluate string in returned ProblemDetail
- CustomerServiceApplicationTests (src/test/java): used WebTestClient to test endpoints; created helper methods for getCustomer(), processTrade; test methods call helper methods and focus on asserting results
- created ExceptionHandler, handleException():ProblemDetail methods for each exception; refactored build():ProblemDetail for use by handleException() methods; updated Sequence Diagram
- created CustomerController (@RestController), 2 endpoints: getCustomerInfo(id):Mono<CustomerInfoDTO> GET /{id}, processTrade(id, Mono<StockTradeRequest>):Mono<StockTradeResponse> POST /{id}/trade; updated Sequence Diagram to reflect Controller
- added to TradeServiceImpl: sellStock(), .executeSell(), refactored balance and portfolio update in executeBuy() to updateBalanceAndPortfolio() to reuse for executeSell(); updated UML sequence diagram for TradeService use cases
- [BP] created <<TradeService>>, TradeServiceImpl with references to repos, (+)processTrade(custId, tradeReq):Mono<StockTradeResponse>, with private methods buyStock(), sellStock(), executeBuy(), executeSell(). buyStock() preps a Mono<Customer> and Mono<PortfolioItem> prior to calling executeBuy. Used switchIfEmpty(), filter(), defaultIfEmpty(), ApplicationExceptions, mapper call to get default new PortfolioItem; executeBuy() sets fields accordingly and does repos call, calls to repo done in parallel via Mono.zip; Updated Mapper class; update UML sequence diagram
- refactored DTO naming; renamed private method in Service impl. [BP] template for entities with entity collection inside entity, with dto containing both parent entity and entity collection info. e.g. Order/OrderItem
- created <<CustomerService>>, CustomerServiceImpl with (+)findCustomerInfoById(id):Mono<CustomerInfo> making a lambda call to this method ->, (-) getCustomerInfo(customer):Mono<CustomerInfo>; create CustomerMapper with toCustomerInfo(customer, List<PortfolioItem>):CustomerInfo
- created Exception classes: CustomerNotFoundException, InsufficientBalanceException, InsufficientSharesException, ApplicationExceptions with factory methods
- created domain: (enum) Ticker, TradeAction; entity: Customer, PortfolioItem; dto: (record) Holding, CustomerInfo, StockTradeRequest, StockTradeResponse; repository: <<CustomerRepository>>, <<PortfolioRepository>>
- create packages: advice, controller, domain, dto, entity, exceptions, mapper, repository, service; application.yaml, logback.xml, data.sql
- initial proj create, SpringBoot 3.5.0, jdk 21, jar; dep: Spring Data R2DBC, Spring Reactive Web, H2, Lombok

proj: webflux-sandbox

Best Practices
- [BP] HTTP2Test; Enabled HTTP2 in WebClient instance setup with pool size only 1. Processed 30,000 request in 5 secs 
- [BP] Customized Connection Pool to handle more request: Consumer<WebClient.Builder>, ConnectionProvider, .lifo(), .maxConnections(poolsize), .pendingAcquireMaxCount(), .compress(), .clientConnector(), ReactorClientHttpConnector+
- [BP] ConnectPoolTest; test concurrent requests from client to slow service (demo03). created section11 on test package

- Server Sent Events
- added ServerSentEventTest: @SpringBootTest, tested streamProduct endpoint
- Added filtering to Streaming. Created DataSetupService (@Service) implementing <<CommandLineRunner>> to emit 1,000 ProductDTO in 1 second interval with Random prices (via ThreadLocalRandom); new streaming endpoint in ProductController that accepts a @PathVariable (price) and passed to .filter() for filtering the streaming data; used template UI page
- updated ProductController, created 2 endpoints saveProduct(), streamProduct() with produces=MediaType.TEXT_EVENT_STREAM_VALUE; test via browser (streamProduct endpoint), postman/console curl (saveProduct)
- Update service class (interface and impl) to autowire productSink bean; added two api saveProduct(Mono<dto>):Mono<dto> that calls tryEmitNext() to save dto to productSink, and streamProduct():Flux<dto> calling productSink.asFlux()
- create config pkg; created ApplicationConfig (@Configuration); created @Bean productSink():Sinks.Many 
- created section11 package

- Streaming - Uploading million Products
- FluxFileWriter - enables writing to a file as a Flux<String> publisher is being processed; modified test class to call existing downloadProducts() with FluxFileWriter
- Added client method to write records to a file(products.txt) as the Flux<ProductDTO> publisher is being processed; [BP] modified test to process 1,000,000 records
- created service and related API, endpoint/methods for retrieving (download) all products. Updated ProductClient and DownloadTest as well;
- Create ProductsUploadDownloadTest (src/test/java) to use ProductClient. testUpload() creates Flux<ProductDTO> of 10 products and calls client; to run, have springboot service running prior to executing test
- Created ProductClient in src/test/java, defined WebClient reference, uploadProducts() to call appropriate service endpoint. Set .contentType(MediaType.APPLICATION_NDJSON)
- Created UploadResponseDTO; ProductController using NDJSON format (MediaType.APPLICATION_NDJSON_VALUE), uploadProducts() post endpoint method
- Created section10 pkg; created pkgs dto, entity, mapper, repository; create Product entity, ProductDTO record, Mapper, ProductRepository; enabled lombok; created ProductService class (Interface and Impl) with saveProducts(Flux<ProductDTO>):Flux<ProductDTO> and getProductsCount():Mono<Long> 

- WebClient - Non-Blocking HTTP Client
- modified LoggingFilter to execute only given the value of an attribute (ClientRequest.attributes()),
attribute set in call to WebClient via RequestHeader.attribute(); calls logging only for even Id numbers passed in request
- Section09AssignmentLoggingFilter - created separate logging filter to log request info; added new filter to filter chain call
- [BP] Section09ExchangeFilterTest - generate token for each request sent; used ExchangeFilterFunction, tokenGenerator():ExchangeFilterFunction, ClientRequest.from(), ExchangeFunction.exchange(), filter(ExchangeFilterFunction):Builder
- Section09BearerAuthTest - for sending request requiring bearer token; call defaultHeaders(consumer), setBearerAuth()
- Section09BasicAuthTest - for sending request requiring basic auth in header; call createWebClient(consumer) calling defaultHeaders(consumer), setBasicAuth()
- Section09QueryParamTest - for sending HTTP requests having query params; used UriBuilder (w/variables and [BP] with Map) - uri(), .path(), .query(), build(10,5,"+"), build(map)
- [BP] Section09ErrorHandlingTest: exchangeTest() using .exchangeToMono(this::decode), custom eval logic of ClientResponse
- [BP] created Section09ErrorHandlingTest; doOnError(Ex, consumer) to log exception in ProblemDetail format via ex.getResponseBodyAs(), onErrorReturn(WebClientResponseException, obj) for error recovery returning obj appropriate for given WCRException Type: InternalServerError, BadRequest
- created Section09HeaderTest - request requiring header values. WebClient.Builder.defaultHeader() - set via factory method of WebClient impl, .header(), .headers(consumer) (h->h.setAll(map))
- created Section09FluxTest - send POST request to external service, via 2 options: passing in bodyValue (bodyValue(ProductDTO)), passing in Body Publisher (body(Mono<ProductDTO>))
- created Section09StreamTest - process streaming response from external service endpoint; used bodyToFlux()
- Section09MonoTest: added concurrentRequest() (lecturers code) and provided my improved alternative [BP] concurrentRequestsTest using Flux.range(), flatMap(function), getProduct(id):Mono<ProductDTO> - execute concurrent requests
- created test Section09MonoTest to connect to external service via AbstractWebClient. tested GET /lec01/product/{id}, retrieved a Mono<ProductDTO>; created ProductDTO record to capture response data from external service
- created section9 package in src/test/java; created AbstractWebClient [for demo02 endpoints - ensure external service is running]: WebClient, createWebClient(consumer<>):WebClient, createWebClient():WebClient, print():<T> Consumer<T>; ensure external service (docker) is running

- Functional Endpoints
- answered assignment #92: [BP] on use of higher order functions to write RouterFunctions and Handlers; used RequestPredicate, used other impl of RouterFunctions.route().GET()
- created RouterFunction Filters: filter()
- modified RouterConfiguration to nest route functions, use path()
- modified order of route for "GET /customers/{id}", and "GET /customers/paginated" to prevent calling the 1st GET when paginated GET is expected
- modified handler getAllCustomersPaginated() to return complete collection instead of stream as is appropriate with pagination
- created UML sequence and class diagrams to include error handling structure and flow
- Created ApplicationExceptionHandler (@Service), handleException() for each exception type; wire handler to RouterConfiguration, used onError() and chained for each exception type. Take note of [BP] using high-order functions encapsulating common handleException() logic.
- Created RouterConfiguration, implemented route mappings for endpoints - customerRoutes():RouterFunction<ServerResponse>, each route mapping is a map of endpoint url and respective handler method
- created UML diagram for section 8: class diagram and sequence diagram centered on RouterConfiguration and CustomerRequestHandler
- created CustomerRequestHandler, implemented handler methods returning Mono<ServerResponse>. called Service using inputs from ServerRequest
- created RouterConfiguration, CustomerHandler.getAllCustomers()
- added section8 package set for Functional Endpoints study

- WebFilter
- added section7.CustomerServiceTest to test stamdard amd prime category and HttpStatus: OK 200, FORBIDDEN 403, UNAUTHORIZED 401
- created FilterErrorHandler to handle errors thrown from WebFilter returning ProblemDetail wrapped in a Mono<Void>. WebFilters call handler's, sendProblemDetail() to do return
- created UML diagrams for section 7: class diagram and sequence diagram for filter behavior 
- modified controller getAll() to get category via @RequestAttribute
- created AuthorizationWebFilter, retrieved category via exchange.getAttributeOrDefault(), provide method handler for enum category values, do a switch in filter() calling appropriate method handler for case representing enum bategory val; modified AuthenticationWebFilter to save category as an attribute
- created AuthenticationWebFilter that checks for auth-token (determines access) in header; extracted token from header via exchange.getRequest().getHeaders().getFirst(); setStatusCode in response via exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED); reorganized uml diagrams
- Added package section6 for integrating filters; created 2 x classes implementing <<WebFilter>>, used filter(), @Order

- Input Validation and Error Handling
- Added CustomerServiceTest for section6 to test modifications to error handling
- modified sequence diagram for update and save customer flows
- Added ExceptionHandler (@ControllerAdvice), added handleException()s (@ExceptionHandler) for CustomerNotFoundException/InvalidInputException.class; modified classes to remove lombok references
- included sequence diagrams (plantuml/mermaid) for saveCustomer(), updateCustomer() validation flow. note: please use plantuml or mermaid preview plugin or app to view.
- created RequestValidator which encapsulate validation logic and throws approriate Error monos. CustomerController.update()/save() (note: request body) invokes transform() on Mono<dto> using RequestValidator as an arg, transform() ultimately returns an error mono or dto mono.
- created custom Exception types ext RuntimeException: CustomerNotFoundException and InvalidInputException; created factory class ApplicationExceptions, with factory methods returning Mono.error enclosing specific exception type mentioned
- created new package(section6) for demoing Input Validation/Error Handling. Added package placeholders: advice, exceptions, validator

package jayslabs.core.practice:
- HigherOrderFunctions class: wrote closure and callback functions; refer to HOF notes in core java notes

proj: webflux-sandbox
- added tests to CustomerServiceTest: validateCustomerNotFound() 404 using isNotFound() and 4xxClientError() for get,put,delete endpoints 
- added tests to CustomerServiceTest: getCustomerById(), createCustomer(), deleteCustomer(), updateCustomer()
- modified CustomerController - deleteCustomer API to return 204;
- modified CustomerController - saveCustomer API to return 201;
- added getCustomerId test
- created CustomerServiceTest: uses WebTestClient and test getAllUsers() api and getAllUsers paginated api
- created paginated get API - uses @RequestParam for page and size; defined findBy() in <<Repository>> which uses Pageable. defined Service method which uses PageRequest.Of() 
- modified deleteCustomer() api to return 200 and run map() only on successful repo delete
- added Query method to repository to return Mono<Boolean> for deleteCustomer() and return 204 instead of 200 for successful delete; modified controller and service
- [BP] updated APIs getCustomerById() and updateCustomer() to use ResponseEntity to return 404. modified return to Mono<ResponseEntity<CustomerDTO>>
- fixed issue with autowiring: added missing mapstruct-processor to pom, constructor for repo and mapper in ServiceImpl
- created controller tailored for reactive pipeline, with CRUD APIs. Take note of Mono<> in @RequestBody for saveCustomer() and updateCustomer()
- create <<ICustomerService>> and CustomerServiceImpl: 
    - getAllCustomers():Flux<CustomerDTO>
    - getCustomerById(Integer id):Mono<CustomerDTO>
    - saveCustomer(Mono<CustomerDTO>):Mono<CusomterDTO>
    - updateCustomer(Integer id, Mono<CustomerDTO> mono):Mono<CustomerDTO>
    - deleteCustomer(Integer id):Mono<Void>
- created Customer Mapper: basic java class, MapStruct interface
- created demo section for REST API with Webflux; created packages structure: controller; service, repository, dto, mapper, entity; CustomerDTO, Customer, <<CustomerRepository>>

proj: [BP][Template Quality] consumption-throughput-demo
- created: parent mvn proj, pom, docker compose file, customer.sql, Makefile: 2 sub modules, reactive/traditional: customer entity, repository interfaces, 2x runners (CommandLineRunner - Efficiency/Throughput TestRunner), app.yml  
- demo project to test R2DBC vs JPA on efficiency (bulk querying) and throughput (speed of query execution) on 10,000,000 records on postgresql (setup via docker)


proj: webflux-sandbox
- used DatabaseClient instead of r2dbc repo to execute custom sql: dbClient.sql(), bind(param, val), mapProperties(dto class), all() 
- updated CustomerOrderRepo to add method to return DTO from three tables; added test getCustomerOrdersByProductDescription(desc)
- [BP] updated CustomerOrderRepository to add method for custom query: @Query, getProductsOrderedByCustomerName(name):Flux<Product>; added CustomerOrderRepoTest, getProductsOrderedByCustomerName()
- Created CustomerOrder entity: used UUID for Id, [BP] java.time.Instant for orderDate (timestamp); created <<CustomerOrderRepository>>; refactored repositories to add @Repository
- Pagination in r2dbc: modified ProductRepository, added findAllBy(Pageable):Flux<Product>; added test findPageableAllBy - test to return page 1 with 3 items per pg: used PageRequest, Sort.by("price"), ascending()
- done Assignment25: create <<ProductRepository>>, add findByPriceBetween(), create ProductRepositoryTest, create testFindByPriceBetween()
- tested setting logging level on app.yml and via @SpringBootTest property
- created test to update; demo update logic: findByX(str), doOnNext(function to modify), flatMap(repo.save)
- created test for Customer repo insert and delete: save(cust):Mono<Customer>, deleteById(id), count() 
- Assignment21: Modified CustomerRepository to add custom query method: finByEmailEndingWith(str):Flux<Customer>, added test: findByEmailEndingWith()
- created Customer entity: used Lombok; created <<CustomerRepository>> extending ReactiveCrudRepository<Customer,Integer>, added repo method findByName(name):Flux<Customer>; created Test Class to test CustRepo, @SpringBootTest, 3x testcases (@Test) - testFindAll(), testFindById(), testFindByName()
- added code to use R2DBC with H2; app.yml, data.sql; modified WebFluxSandboxApplication: used property scanBasePackages, added @EnableR2dbcRepositories/basePackages; refactored package naming
- demoed Resiliency for Reactive endpoints; used new External service endpt to throw error, added onErrorComplete() to enable completion of emit
- Created Streaming endpoint on reactive controller; used org.springframework.http.MediaType, MediaType.TEXT_EVENT_STREAM_VALUE
- TraditionalProductController, ReactiveProductController (Traditional and Reactive controllers); used web.client.RestClient, reactive.function.client.WebClient, .builder().requestFactory().baseUrl().build(), .get().uri().retrieve(), .body(), .bodyToFlux(), doOnNext(). Flux<Product>, Product record 
- created package, tradvsreactive02 for demo on traditional vs reactive service; Product record,
- initial proj commit: generate via initialzr (jdk 21, springboot 3.4.4; dep: Spring Reactive Web, Spring Data R2DBC, H2); created dockerfile, image for exeternal-service.jar

repo:
- initial proj commit
- created local and gh repo : reactive-microservices
