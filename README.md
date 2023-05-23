# Libraries
Java 17
SpringBoot 3.1.0
Mockito
Hamcrest
Junit 5


# To run the application:
mvn package
java -jar target/bookstore-0.0.1-SNAPSHOT.jar


# Swagger UI
You can open swagger-ui in your browser and execute the endpoint.
http://localhost:8080/swagger-ui/index.html

# Comments:
In this demo project I process author and publisher in create/update book endpoint. 
I match them by name. A potential problem is same publisher/author can be written in different ways.

In a fully-featured app I would pass author, publisher ids to these endpoints.







