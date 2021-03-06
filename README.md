# Product List
This application goes to Sainsbury's products page.
Take all products listed on the page, goes to each product page and return a list of products with the following content:
```json
{
  "products": [
    {
      "title": "Sainsbury's Apricot Ripe & Ready x5",
      "size": "514kb",
      "description": "Apricots",
      "unit_price": 3.5
    },
    {
      "title": "Sainsbury's Avocado Ripe & Ready XL Loose 300g",
      "size": "514kb",
      "description": "Avocados",
      "unit_price": 1.5
    },
    ...
  ],
  "total": 15.1
}
```
### Stack
- [java 8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html) - Programming language
- [spring-boot 1.4.2](https://github.com/spring-projects/spring-boot) - Provide application services
- [tomcat](http://tomcat.apache.org/) - Web container server, embeded in spring-boot
- [jsoup 1.10.1](https://jsoup.org/) - Java library for html parser
- [junit](http://junit.org/) - Unit tests
- [mockito](http://site.mockito.org/) - Mocking objects for unit tests
- [SpringJunit](http://docs.spring.io/spring-batch/reference/html/testing.html) - Unit test that depends on the context
- [Jackson](http://wiki.fasterxml.com/JacksonHome) Java library for processing JSON data format.

### Setup
1. Install [Java 8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html), if you don't have it yet.
2. Install [maven](https://maven.apache.org/index.html), if you don't have it yet.

### Build
**Building in a local environment:**
- Clone the code from the repository:
- Open the terminal go to some folder and type
```
git clone https://github.com/gianisegatto/products.git
```
- Open the terminal
- Go to the folder that the code was cloned and type
```
mvn clean install
```
### Run console application
- Open the terminal
- Go to the folder that the code was cloned and type
```
1 - cd product-consumer-console/target
Sync execution
2 - java -cp product-consumer-console-1.0-SNAPSHOT.jar com.sainsburys.productconsumer.console.ListProducts
Async execution
2 - java -cp product-consumer-console-1.0-SNAPSHOT.jar com.sainsburys.productconsumer.console.ListProductsAsync
```

### Run Server application
- Open the terminal
- Go to the folder that the code was cloned and type
```
1 - cd product-consumer-app
2 - mvn spring-boot:run
```
Or
- Open the terminal
- Go to the folder that the code was cloned and type
Note: If the application is already built go directly to step 2
```
1 - mvn clean install
2 - java -jar product-consumer-app/target/product-consumer-app-1.0-SNAPSHOT.jar
```
### Executing
#### Call Sync service
- Open the terminal and type
```
curl http://localhost:8080/products
```
#### Call Async service
- Open the terminal and type
```
curl http://localhost:8080/products/async
```
- This service is using CompletableFuture java feature to turn the code non-blocking

### Motivation to create two services sync and async
The motivation of build two differents services that do the same thing is showing the difference between reactive code and common code.
If you open the code in some Idea and put a breakpoint on lines 61 and 74 inside the method and startup the application on debug mode and call the async service. You can see the non-blocking code running.

### Note
All expections are logged but ignored when it happens I'm returning null or empty list. It could be better if the result had an alerts or erros element to be retorned information about the itens that had problem.

### The services also can be called from the [Postman](https://www.getpostman.com/)
Use the folder postman and import the collection to your postman app.
This colletion provide call to both services sync and async and assertation test for the result.
- Execute the Run step
- Open the postman
- Click on the import button
- Select the file 
```
PROJECT-FOLDER/postman/ProductsColletion.json
```
- Go to the Products colletion
- Click on the > button
- Click on the Run button
- After collection runner window open click on Start Run button
