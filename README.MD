
mvn clean package -DskipTests  

docker build --tag=task_app:latest .

docker-compose up

