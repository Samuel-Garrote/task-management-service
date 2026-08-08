# Task Management API
## 🏛️ Distributed System Architecture

```mermaid
flowchart TD
    %% Node Styles
    classDef client fill:#3b82f6,stroke:#1d4ed8,stroke-width:2px,color:#fff;
    classDef service fill:#10b981,stroke:#047857,stroke-width:2px,color:#fff;
    classDef infra fill:#f59e0b,stroke:#b45309,stroke-width:2px,color:#fff;
    classDef db fill:#8b5cf6,stroke:#6d28d9,stroke-width:2px,color:#fff;

    Client["📱 / 💻 Web / Mobile Client"]:::client

    subgraph System ["Microservices Ecosystem"]
        
        subgraph Auth_MS ["1. Auth Microservice"]
            AuthController["AuthController"]
            AuthService["AuthService (JWT)"]
            AuthDB[("Auth DB (Users)")]:::db
            AuthController --> AuthService --> AuthDB
        end

        subgraph Core_MS ["2. Task Core Microservice"]
            TaskController["TaskController"]
            TaskService["TaskService (Hexagonal)"]
            TaskProducer["KafkaTaskProducer"]
            TaskDB[("Task DB (Tasks/Projects)")]:::db

            TaskController --> TaskService
            TaskService --> TaskDB
            TaskService --> TaskProducer
        end

        subgraph Messaging ["Message Broker"]
            KafkaBroker{{"Apache Kafka Topic: task-events"}}:::infra
            KafkaDLQ{{"Kafka Topic: task-events.DLQ"}}:::infra
        end

        subgraph Worker_MS ["3. Notification / Worker Microservice"]
            KafkaConsumer["KafkaTaskConsumer"]
            IdempotencyCheck{"Idempotency Strategy<br/>(Deduplication)"}:::infra
            ProcessService["Notification / Processing Service"]
            WorkerDB[("Worker DB / Cache")]:::db

            KafkaConsumer --> IdempotencyCheck
            IdempotencyCheck -- "Duplicate Event" --> Skip["🛑 Ignore / Direct ACK"]
            IdempotencyCheck -- "New Event" --> ProcessService
            ProcessService --> WorkerDB
        end

    end

    %% Communication Flows
    Client -- "1. Login / Register" --> AuthController
    Client -- "2. HTTP Request (Bearer JWT)" --> TaskController
    TaskProducer -- "3. Publish TaskCreatedEvent" --> KafkaBroker
    KafkaBroker -- "4. Consume Event (At-Least-Once)" --> KafkaConsumer
    KafkaConsumer -- "5. Failure after N retries" --> KafkaDLQ
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
