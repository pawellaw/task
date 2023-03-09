FROM openjdk:17
MAINTAINER task.example.com
COPY target/task-0.0.1-SNAPSHOT.jar task_app.jar
ENTRYPOINT ["java","-jar","/task_app.jar"]