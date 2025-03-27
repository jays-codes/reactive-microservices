# reactive-microservices
Jay's project/practice repo for Reactive Microservices : WebFlux + Project Reactor

proj: webflux-sandbox
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
