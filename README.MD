# Task Async Processing Application

## Technical stack
- Java 17
- Spring Boot 3
- Tests : Groovy, Spock, JUnit, Awaitility

## Run Tests

`./mvnw test`

## How to run application using docker compose

### System requirements
- Java JDK 17
- Docker > 20
- Docker-Compose > 2.15
- Maven > 3 

### Commands to run
`./mvnw clean package -DskipTests`  

`docker build --tag=task_app:latest .`

`docker-compose up`

## OpenAPI Description (after application start)

http://localhost:8888/swagger-ui/index.html

## Postman Collection

Postman files to import and use:

`New Environment.postman_environment.json`
`Tasks.postman_collection.json`

