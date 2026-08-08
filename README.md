# Task Management API
## 🏛️ Task Managment API Architecture (Single Service / Hexagonal)

```mermaid
flowchart TD
    %% Node Styles
    classDef client fill:#3b82f6,stroke:#1d4ed8,stroke-width:2px,color:#fff;
    classDef adapter fill:#10b981,stroke:#047857,stroke-width:2px,color:#fff;
    classDef domain fill:#f59e0b,stroke:#b45309,stroke-width:2px,color:#fff;
    classDef infra fill:#8b5cf6,stroke:#6d28d9,stroke-width:2px,color:#fff;

    Client["📱 / 💻 Web Client / Postman"]:::client

    subgraph App ["TaskMaster API (Monolithic Hexagonal App)"]

        subgraph Security ["Security & JWT (Vueltas 5+6)"]
            SecurityConfig["SecurityConfig"]
            JwtAuthFilter["JwtAuthFilter"]
            AuthController["AuthController (/auth/login, /auth/register)"]
            AuthService["AuthService"]
            JwtService["JwtService"]
            
            SecurityConfig -. "Registers filter" .-> JwtAuthFilter
            AuthController --> AuthService
            AuthService --> JwtService
        end

        subgraph WebLayer ["Primary Adapters / Drivers"]
            TaskController["TaskController (5 Endpoints) + @Valid"]:::adapter
            GlobalHandler["GlobalExceptionHandler"]:::adapter
        end

        subgraph DomainLayer ["Domain & Application Core (Vueltas 1, 2, 3, 7)"]
            UseCases["Input Ports<br/>(CreateTaskUseCase, FindTaskByIdUseCase...)"]:::domain
            TaskService["TaskService (Core Logic)"]:::domain
            DomainModels["Entities & Aggregates<br/>(Task, Project, User)"]:::domain
            
            UseCases --> TaskService
            TaskService --> DomainModels
        end

        subgraph OutAdapters ["Secondary Adapters / Driven"]
            
            subgraph Persistence ["Persistence Adapter"]
                TaskRepoPort["TaskRepositoryPort / UserRepositoryPort"]:::adapter
                TaskRepoAdapter["TaskRepositoryAdapter / UserRepositoryAdapter"]:::adapter
                Mappers["Mappers (EntityMappers)"]:::adapter
                JPARepos["JPA Repositories"]:::adapter
                
                TaskRepoPort --> TaskRepoAdapter
                TaskRepoAdapter --> Mappers
                TaskRepoAdapter --> JPARepos
            end

            subgraph Messaging ["Kafka Adapter (Vuelta 8)"]
                TaskEventProducer["TaskEventProducer"]:::adapter
                TaskEventConsumer["TaskEventConsumer (@KafkaListener)"]:::adapter
                KafkaErrorConfig["KafkaErrorConfig (DefaultErrorHandler)"]:::adapter
            end

        end

        subgraph TestingSuite ["Testing Suite (Vuelta 9)"]
            UnitTests["TaskServiceTest (Mockito - 7 tests)"]
            WebTests["TaskControllerTest (@WebMvcTest - 2 tests)"]
        end

    end

    subgraph External ["External Infrastructure"]
        DB[("Database (JPA)")]:::infra
        KafkaBroker{{"Apache Kafka Topic: task-events"}}:::infra
        KafkaDLQ{{"Kafka Topic: DLQ"}}:::infra
    end

    %% Flow Connections (Runtime)
    Client -- "1. HTTP Request (JWT Bearer)" --> JwtAuthFilter
    JwtAuthFilter -- "2. Authenticated" --> TaskController
    TaskController -- "3. DTO" --> UseCases
    TaskService -- "4. Save Task" --> TaskRepoPort
    JPARepos -- "5. Persist" --> DB
    
    TaskService -- "6. Emit Event" --> TaskEventProducer
    TaskEventProducer -- "7. Publish TaskCreatedEvent" --> KafkaBroker
    KafkaBroker -- "8. Consume Event" --> TaskEventConsumer
    TaskEventConsumer -- "9. If fails N times" --> KafkaErrorConfig
    KafkaErrorConfig -- "10. Route to DLQ" --> KafkaDLQ
```

A RESTful task management API built with Spring Boot 4, following hexagonal architecture principles. Supports task and project management with JWT-based authentication and event-driven notifications via Apache Kafka.

## Features

- **CRUD operations** for tasks and projects, with relationships between them
- **Hexagonal architecture** — clear separation between domain, application, and infrastructure layers
- **JWT authentication** — user registration, login, and protected endpoints
- **Event-driven messaging** — publishes `TaskCreatedEvent` to Kafka on task creation, with a dedicated consumer and dead-letter queue (DLQ) error handling
- **Input validation** with descriptive error responses via a global exception handler
- **Unit and integration tests** — service layer tested with JUnit 5 and Mockito, controller layer tested with MockMvc

## Tech Stack

- **Java 21**
- **Spring Boot 4** (Spring Framework 7)
- **Spring Data JPA** + H2 (in-memory database)
- **Spring Security** + JWT (jjwt)
- **Apache Kafka** (Spring for Apache Kafka)
- **JUnit 5 / Mockito / AssertJ**
- **Docker** (multi-stage build)
- **Maven**

## Architecture

The project follows hexagonal (ports & adapters) architecture:

domain/ → Core business models (Task, Project, User)
app/
port/in/ → Input ports (use case interfaces)
port/out/ → Output ports (repository interfaces)
service/ → Application services implementing the use cases
infra/
in/web/ → REST controllers, DTOs, mappers
out/persistence/ → JPA entities, repositories, adapters
out/messaging/ → Kafka producers/consumers
security/ → JWT filter and service
config/ → Security and Kafka configuration

## API Endpoints

| Method | Endpoint              | Description                  | Auth required |
|--------|------------------------|-------------------------------|----------------|
| POST   | `/auth/register`       | Register a new user           | No             |
| POST   | `/auth/login`          | Authenticate and get a JWT    | No             |
| POST   | `/tasks`                | Create a task                 | Yes            |
| GET    | `/tasks/{id}`           | Get a task by ID              | Yes            |
| PUT    | `/tasks/{id}`           | Update a task                 | Yes            |
| DELETE | `/tasks/{id}`           | Delete a task                 | Yes            |
| GET    | `/tasks/project/{id}`   | Get all tasks for a project   | Yes            |

## Running Locally

```bash
mvn clean package
docker build -t task-management-service .
docker run -p 8080:8080 task-management-service
```

The API will be available at `http://localhost:8080`.

## Live Demo

Deployed on AWS EC2 using Docker. All `/tasks/**` endpoints require JWT authentication.

> Note: uses an in-memory H2 database, so data resets on container restart.

## Testing

```bash
mvn test
```

Covers the service layer (business logic, mocked repository/Kafka producer) and the controller layer (HTTP status codes, request validation).
