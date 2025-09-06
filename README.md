# Random Quote API with Rate Limiting

A RESTful API built with Spring Boot that provides random inspirational quotes with IP-based rate limiting to prevent abuse.

## Features

- **Random Quotes**: Returns inspirational quotes from a curated collection
- **Rate Limiting**: IP-based rate limiting (5 requests per minute per IP)
- **Thread-Safe**: Uses Bucket4j for concurrent rate limiting
- **API Documentation**: Swagger/OpenAPI integration
- **Logging**: Comprehensive request logging
- **Health Checks**: Built-in health endpoint
- **Unit Tests**: Comprehensive test coverage

## Tech Stack

- **Java 17**
- **Spring Boot 3.1.5**
- **Bucket4j 8.7.0** - For rate limiting
- **SpringDoc OpenAPI** - For API documentation
- **Maven** - Build tool
- **JUnit 5** - Testing framework

## Project Structure

```
quote-api/
├── src/
│   ├── main/
│   │   ├── java/com/example/quoteapi/
│   │   │   ├── QuoteApiApplication.java       # Main application class
│   │   │   ├── controller/
│   │   │   │   └── QuoteController.java       # REST controller
│   │   │   ├── service/
│   │   │   │   ├── QuoteService.java          # Quote business logic
│   │   │   │   └── RateLimitingService.java   # Rate limiting logic
│   │   │   ├── model/
│   │   │   │   ├── QuoteResponse.java         # Quote response model
│   │   │   │   └── ErrorResponse.java         # Error response model
│   │   │   └── config/
│   │   │       └── OpenApiConfig.java         # Swagger configuration
│   │   └── resources/
│   │       └── application.properties         # Application configuration
│   └── test/                                  # Unit tests
├── pom.xml                                    # Maven dependencies
└── README.md                                  # This file
```

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher

### Installation & Setup

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd quote-api
   ```

2. **Build the project:**
   ```bash
   mvn clean compile
   ```

3. **Run tests:**
   ```bash
   mvn test
   ```

4. **Start the application:**
   ```bash
   mvn spring-boot:run
   ```

   Or alternatively:
   ```bash
   mvn clean package
   java -jar target/quote-api-1.0.0.jar
   ```

5. **Verify the application is running:**
   - The API will be available at: `http://localhost:8080`
   - Health check: `http://localhost:8080/api/health`
   - Swagger UI: `http://localhost:8080/swagger-ui.html`

## API Endpoints

### Get Random Quote
- **URL**: `/api/quote`
- **Method**: `GET`
- **Description**: Returns a random inspirational quote
- **Rate Limit**: 5 requests per minute per IP address

**Success Response (200):**
```json
{
  "quote": "The only way to do great work is to love what you do. - Steve Jobs"
}
```

**Rate Limit Exceeded (429):**
```json
{
  "error": "Rate limit exceeded. Try again in 60 seconds."
}
```

### Health Check
- **URL**: `/api/health`
- **Method**: `GET`
- **Description**: Returns API health status

**Response (200):**
```json
{
  "status": "UP",
  "message": "Quote API is running"
}
```

## Testing the API

### Using cURL

1. **Get a random quote:**
   ```bash
   curl http://localhost:8080/api/quote
   ```

2. **Test rate limiting** (run this command more than 5 times within a minute):
   ```bash
   for i in {1..7}; do echo "Request $i:"; curl http://localhost:8080/api/quote; echo "\\n"; done
   ```

3. **Health check:**
   ```bash
   curl http://localhost:8080/api/health
   ```

### Using Postman

1. Import the API endpoints or use the Swagger UI to explore
2. **GET** `http://localhost:8080/api/quote`
3. Send multiple requests quickly to test rate limiting
4. **GET** `http://localhost:8080/api/health` for health check

### Rate Limiting Test

To test the rate limiting functionality:
```bash
# This script will make 7 requests and show rate limiting in action
for i in {1..7}; do
  echo "Request $i:"
  curl -w "HTTP Status: %{http_code}\\n" http://localhost:8080/api/quote
  echo "---"
done
```

Expected behavior:
- First 5 requests: HTTP 200 with quotes
- Requests 6+: HTTP 429 with rate limit error

## Configuration

### Rate Limiting Settings
The rate limiting is configured in `RateLimitingService.java`:
- **Capacity**: 5 requests per IP
- **Refill Period**: 1 minute
- **Refill Amount**: 5 tokens

### Application Properties
Key settings in `application.properties`:
```properties
server.port=8080
spring.application.name=quote-api
logging.level.com.example.quoteapi=INFO
```

## API Documentation

Once the application is running, you can access:
- **Swagger UI**: http://localhost:8080/swagger-ui.html

The Swagger UI provides:
- Interactive API documentation
- Request/response examples
- Try-it-out functionality

## Design Decisions

### Rate Limiting Implementation
- **Bucket4j**: Chosen for its thread-safety and token bucket algorithm
- **ConcurrentHashMap**: Used for storing IP-to-bucket mappings in memory
- **IP-based**: Rate limiting based on client IP address with proxy support

### Quote Storage
- **In-memory**: Quotes stored in a Java List for simplicity
- **No Database**: As per requirements, no persistent storage used
- **Random Selection**: Uses Java's Random class for quote selection

### Logging Strategy
- **Structured Logging**: IP address and HTTP status logged for each request
- **SLF4J**: Industry-standard logging framework
- **Log Levels**: INFO for successful requests, WARN for rate-limited requests

### Thread Safety
- **Bucket4j**: Provides thread-safe rate limiting
- **ConcurrentHashMap**: Thread-safe IP-to-bucket mapping
- **Stateless Services**: Services designed to be thread-safe

## Testing

Run the test suite:
```bash
mvn test
```

The project includes:
- **Unit Tests**: Service layer testing
- **Integration Tests**: Controller layer testing with MockMvc
- **Rate Limiting Tests**: Specific tests for rate limiting logic

## Production Considerations

For production deployment, consider:

1. **Distributed Rate Limiting**: Use Redis for rate limiting across multiple instances
2. **Database**: Store quotes in a database for easier management
3. **Monitoring**: Add metrics and monitoring (Micrometer/Prometheus)
4. **Security**: Add authentication and HTTPS
5. **Caching**: Cache quotes for better performance
6. **Configuration**: Externalize rate limiting configuration

## Troubleshooting

### Common Issues

1. **Port 8080 already in use:**
   ```bash
   # Change port in application.properties
   server.port=8081
   ```

2. **Java version mismatch:**
   ```bash
   # Check Java version
   java -version
   # Should be Java 17 or higher
   ```

3. **Maven build issues:**
   ```bash
   # Clean and rebuild
   mvn clean install
   ```
