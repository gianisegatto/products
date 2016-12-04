# Products List

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
### Setup

1. Install [Java 8](http://www.oracle.com/technetwork/java/javase/overview/java8-2100321.html), if you don't have it yet.

2. Install [maven](https://maven.apache.org/index.html), if you don't have it yet.

### Build

**Building in a local environment:**

Clone the code from the repository:
https://github.com/gianisegatto/products.git

Open the terminal
Go to the folder that the code was cloned and type
```
mvn clean install
```

### Run

Open the terminal
Go to the folder that the code was cloned and type

```
mvn spring-boot:run
```
### Call Sync service
Open the terminal and type

```
curl http://localhost:8080/products
```

### Call Async service
Open the terminal and type

```
curl http://localhost:8080/products/async
```
