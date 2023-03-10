# Task Async Processing Application

## Technical stack
- Java 17
- Spring Boot 3
- Tests : Groovy, Spock, JUnit, Awaitility

## Run Tests 

`mvn test`

## How to run application using docker compose
`mvn clean package -DskipTests`  

`docker build --tag=task_app:latest .`

`docker-compose up`

## OpenAPI Description

http://localhost:8888/swagger-ui/index.html

