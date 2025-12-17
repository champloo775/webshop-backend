# Webshop Backend - Simple E-commerce API

A simple webshop backend application built with Java and Spring Boot. This project provides a RESTful API for managing products and orders with in-memory storage.

## Features

- **Product Management**: Browse and view product details
- **Order Processing**: Create orders with validation and stock management
- **In-Memory Storage**: Uses Java Collections for data persistence
- **Error Handling**: Comprehensive exception handling with meaningful error messages
- **Unit Testing**: Complete test coverage for business logic

## Technology Stack

- **Java 17**
- **Spring Boot 3.2.0**
- **Maven** (build tool)
- **JUnit 5** & **Mockito** (testing)

## Project Structure

```
com.example.webshop/
├── controller/           # REST Controllers
│   ├── ProductController.java
│   └── OrderController.java
├── service/             # Business Logic
│   ├── ProductService.java
│   └── OrderService.java
├── model/               # Data Models
│   ├── Product.java
│   ├── Order.java
│   ├── OrderItem.java
│   ├── CustomerInfo.java
│   ├── OrderRequest.java
│   └── OrderItemRequest.java
├── repository/          # In-Memory Storage
│   ├── ProductRepository.java
│   └── OrderRepository.java
└── exception/           # Custom Exceptions
    ├── ProductNotFoundException.java
    ├── InsufficientStockException.java
    ├── InvalidOrderException.java
    └── GlobalExceptionHandler.java
```

## Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

## Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/champloo775/webshop-backend.git
cd webshop-backend
```

### 2. Build the Project

```bash
mvn clean install
```

### 3. Run the Application

```bash
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### 4. Run Tests

```bash
mvn test
```

## API Documentation

### Base URL
```
http://localhost:8080/api
```

### Endpoints

#### 1. Get All Products

**Request:**
```http
GET /api/products
```

**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "name": "Laptop",
    "description": "High-performance laptop for professionals",
    "price": 12999.99,
    "imageUrl": "https://example.com/laptop.jpg",
    "stock": 15
  },
  {
    "id": 2,
    "name": "Smartphone",
    "description": "Latest smartphone with advanced features",
    "price": 7999.99,
    "imageUrl": "https://example.com/smartphone.jpg",
    "stock": 25
  }
]
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/products
```

---

#### 2. Get Product by ID

**Request:**
```http
GET /api/products/{id}
```

**Response:** `200 OK`
```json
{
  "id": 1,
  "name": "Laptop",
  "description": "High-performance laptop for professionals",
  "price": 12999.99,
  "imageUrl": "https://example.com/laptop.jpg",
  "stock": 15
}
```

**Error Response:** `404 Not Found`
```json
{
  "timestamp": "2024-12-17T10:30:00",
  "message": "Product not found with id: 999",
  "status": 404
}
```

**cURL Example:**
```bash
curl -X GET http://localhost:8080/api/products/1
```

---

#### 3. Create Order

**Request:**
```http
POST /api/orders
Content-Type: application/json
```

**Request Body:**
```json
{
  "customerInfo": {
    "name": "John Doe",
    "address": "123 Main Street, Stockholm",
    "email": "john.doe@example.com"
  },
  "items": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 3,
      "quantity": 1
    }
  ]
}
```

**Response:** `201 Created`
```json
{
  "id": 1,
  "customerInfo": {
    "name": "John Doe",
    "address": "123 Main Street, Stockholm",
    "email": "john.doe@example.com"
  },
  "items": [
    {
      "productId": 1,
      "quantity": 2,
      "priceAtPurchase": 12999.99
    },
    {
      "productId": 3,
      "quantity": 1,
      "priceAtPurchase": 1999.99
    }
  ],
  "totalAmount": 27999.97,
  "orderDate": "2024-12-17T10:30:45.123"
}
```

**Error Response - Insufficient Stock:** `400 Bad Request`
```json
{
  "timestamp": "2024-12-17T10:30:00",
  "message": "Insufficient stock for product id: 1. Requested: 100, Available: 15",
  "status": 400
}
```

**Error Response - Invalid Order:** `400 Bad Request`
```json
{
  "timestamp": "2024-12-17T10:30:00",
  "message": "Order must contain at least one item",
  "status": 400
}
```

**cURL Example:**
```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerInfo": {
      "name": "John Doe",
      "address": "123 Main Street, Stockholm",
      "email": "john.doe@example.com"
    },
    "items": [
      {
        "productId": 1,
        "quantity": 2
      }
    ]
  }'
```

## Sample Products

The application initializes with the following products:

| ID | Name | Description | Price (SEK) | Stock |
|----|------|-------------|-------------|-------|
| 1 | Laptop | High-performance laptop for professionals | 12,999.99 | 15 |
| 2 | Smartphone | Latest smartphone with advanced features | 7,999.99 | 25 |
| 3 | Headphones | Wireless noise-cancelling headphones | 1,999.99 | 40 |
| 4 | Keyboard | Mechanical keyboard for gaming and typing | 899.99 | 30 |
| 5 | Mouse | Ergonomic wireless mouse | 499.99 | 50 |
| 6 | Monitor | 27-inch 4K display monitor | 3,499.99 | 20 |
| 7 | Webcam | HD webcam for video conferencing | 799.99 | 35 |
| 8 | External SSD | 1TB portable external SSD | 1,299.99 | 45 |

## Validation Rules

### Order Validation
- Customer information (name, address, email) is required
- Order must contain at least one item
- Product quantity must be greater than 0
- Product must exist in the catalog
- Sufficient stock must be available

### Stock Management
- Stock is automatically reduced when an order is placed
- Orders with insufficient stock are rejected

## Error Handling

The API uses standard HTTP status codes:

- `200 OK` - Successful GET request
- `201 Created` - Successful order creation
- `400 Bad Request` - Invalid request (validation errors, insufficient stock)
- `404 Not Found` - Resource not found
- `500 Internal Server Error` - Unexpected server error

All error responses include:
- `timestamp` - When the error occurred
- `message` - Human-readable error description
- `status` - HTTP status code

## Testing with Postman

1. Import the following endpoints into Postman:
   - GET `http://localhost:8080/api/products`
   - GET `http://localhost:8080/api/products/1`
   - POST `http://localhost:8080/api/orders`

2. For the POST request, use the request body example provided above

3. Test error scenarios:
   - Try ordering more items than available in stock
   - Try ordering a non-existent product
   - Try creating an order without customer information

## Development Notes

- All data is stored in-memory and will be reset when the application restarts
- The application uses Spring Boot's embedded Tomcat server
- No database configuration is required
- Initial product data is loaded automatically on startup

## Future Enhancements

Potential improvements for future versions:
- Database integration (e.g., PostgreSQL, MySQL)
- Authentication and authorization
- Order history for customers
- Product search and filtering
- Admin endpoints for product management
- Payment integration
- Email notifications

## License

This project is created for educational purposes.

## Contact

For questions or feedback, please contact the repository owner.
