# Product List
This application goes against Sainsbury's products page.
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
- [spring-boot 1.4.2](https://github.com/spring-projects/spring-boot) - Provide application services and embeded webcontainer server
- [jsoup 1.10.1](https://jsoup.org/) - Java library for html parser
- [junit](http://junit.org/) - Unit tests
- [mockito](http://site.mockito.org/) - Mocking objects for unit tests
- [SpringJunit](http://docs.spring.io/spring-batch/reference/html/testing.html) - Unit test that depends on the context
### Setup
1. Install [Java 8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html), if you don't have it yet.
2. Install [maven](https://maven.apache.org/index.html), if you don't have it yet.
### Build
**Building in a local environment:**
- Clone the code from the repository:
```
https://github.com/gianisegatto/products.git
```
- Open the terminal
Go to the folder that the code was cloned and type
```
mvn clean install
```
### Run
- Open the terminal
Go to the folder that the code was cloned and type
```
mvn spring-boot:run
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

### The services also can be called from the [Postman](https://www.getpostman.com/)
Use the folder postman and import the collection to your postman app.
This colletion provide call to both services sync and async and assertation test for the result.
- Execute the step Run
- Open the postman
- Click on the import button
- Select the file 
```
PROJECT-FOLDER/postman/SainsburysProductsColletion.json
```
- Go to the Sainsbury's Products colletion
- Click on the > button
- Click on the Run button
- After colletion runner window open click on Start Run button
