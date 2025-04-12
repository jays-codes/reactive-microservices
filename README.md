# reactive-microservices
Jay's project/practice repo for Reactive Microservices : WebFlux + Project Reactor

proj: webflux-sandbox
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
