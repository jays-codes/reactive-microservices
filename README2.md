
# Reactive Microservices Trading Platform
A reactive microservices trading platform built with Spring Boot 3.5.0 and Java 21.

## Architecture
This is a **monorepo** containing independent microservices, each with their own Spring Boot configuration and multi-module structure.

### Project Structure
```
reactive-microservices/ (Git repository - not a Maven project)
├── common-client/          # Shared library (Spring Boot 3.5.0, Java 21)
├── customer-service/       # Customer microservice (Spring Boot 3.5.0, Java 21)
│   ├── customer-client/    # Customer DTOs and interfaces  
│   └── customer-server/    # Customer business logic (executable JAR)
└── aggregator-service/     # Aggregator microservice (Spring Boot 3.5.0, Java 21)
    ├── aggregator-client/  # Aggregator DTOs and interfaces
    └── aggregator-server/  # Aggregator business logic (executable JAR)
```

### Microservices

#### 1. **Common Client** (`common-client/`)
- **Purpose**: Shared domain classes and DTOs
- **Packaging**: JAR library
- **Dependencies**: Minimal (Jackson annotations)
- **Contents**: `Ticker`, `TradeAction` enums

#### 2. **Customer Service** (`customer-service/`)
- **Purpose**: Customer management and trading operations
- **Architecture**: Multi-module with client/server separation
- **Database**: H2 with R2DBC (reactive)
- **Port**: 6060
- **Features**:
  - Customer CRUD operations
  - Portfolio management  
  - Stock trading (BUY/SELL)
  - Reactive data access

#### 3. **Aggregator Service** (`aggregator-service/`)
- **Purpose**: Service orchestration and composition
- **Architecture**: Multi-module with client/server separation  
- **Communication**: Reactive WebClient for inter-service calls
- **Features**:
  - Customer portfolio aggregation
  - Stock price streaming
  - Trade request processing
  - Service-to-service communication

  ## Building and Running

  ### Prerequisites
- Java 21
- Maven 3.9+

### Build Order
Since services depend on each other, build in this order:

```bash
# 1. Build shared library
cd common-client
mvn clean install

# 2. Build customer service 
cd ../customer-service
mvn clean install

# 3. Build aggregator service
cd ../aggregator-service  
mvn clean install
```

### Running Services

```bash
# Start Customer Service (port 6060)
cd customer-service/customer-server
mvn spring-boot:run

# Start Aggregator Service (requires customer service running)
cd ../../aggregator-service/aggregator-server  
mvn spring-boot:run
```

## API Endpoints

### Customer Service (`localhost:6060`)
- `GET /customers/{id}` - Get customer info
- `POST /customers/{id}/trade` - Process trade

### Aggregator Service
- `GET /customers/{id}` - Get customer portfolio with current prices
- `POST /customers/{id}/trade` - Process trade via aggregation
- `GET /stock/price-stream` - Stream real-time stock prices

### Stock Service
- Running on Docker image (ask repo owner)

### Basic Front-end 
- http://localhost:8080/?customer=1#

## Dependencies Between Services

```
aggregator-service
├── aggregator-client (internal)
├── customer-client (external - for DTOs)  
└── common-client (external - for domain)

customer-service
├── customer-client (internal)
└── common-client (external - for domain)

common-client
└── (no dependencies - pure library)
```

## Technology Stack

- **Framework**: Spring Boot 3.5.0
- **Language**: Java 21
- **Reactive**: Spring WebFlux, Project Reactor
- **Database**: H2 with R2DBC (reactive)
- **Communication**: Reactive WebClient
- **Build**: Maven 3.9+
- **Architecture**: Multi-module microservices

## Key Design Principles

1. **Independent Deployability**: Each service has its own Spring Boot parent
2. **Client-Server Separation**: Clean API contracts via client modules
3. **Reactive Programming**: End-to-end reactive stack
4. **Domain-Driven Design**: Clear service boundaries
5. **Shared Dependencies**: Common domain via shared library