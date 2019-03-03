# simplechat
`Simple Chat` implemented with using **Spring Boot + WebSockets technology
STOMP messaging protocol + Maven build tool**


### Testing
 - web layer using Spring’s MockMvc
 - Mockito
 - implemented test for STOMP

### This project implemented following 
Primitive chat where exist few rooms, 
in each room the capacity is two users. (Can be increased in `RoomCapacityBalancer.CONNECTION_LIMIT`)

### Run application
 Using terminal/cmd go to root folder of project and run the application using command `mvnw spring-boot:run`. \
  Or you can build the JAR file with `mvnw clean package`. Then you can run the JAR file:

`java -jar target/simple-chat-SNAPSHOT.jar`
