
## Deployment

Run setup-bank shell file ./setup-bank

 *if you are using mac os system POSTGRES_HOST should be host.docker.internal* 

## Available user: 

username: *user*, password: *password*

## Overview
- **Version**: v0
- **URL**: [[http://localhost:8080/v1/swagger-ui/index.html]()]
  
## Authentication
- **Security Scheme**: Bearer Authentication (JWT)

## Endpoints

### Conversion
- **POST /v1/conversion/convert**
  - **Description**: Convert currencies.
  - **Request**: `ConversionRequest` (currencyFrom, currencyTo, fromValue)
  - **Response**: `ConversionDto` (id, from, to, fromValue, toValue, rateDate, user, createdAt)

### Authentication
- **POST /v1/auth/sign-up**
  - **Description**: Register a new user.
  - **Request**: `RegisterRequest` (firstname, lastname, username, email, password)
  - **Response**: `UserDto` (id, firstname, lastname, username, email)

- **POST /v1/auth/sign-in**
  - **Description**: Authenticate a user.
  - **Request**: `LoginRequest` (username, password)
  - **Response**: `LoginResponse` (jwttoken)

### Users
- **GET /v1/users**
  - **Description**: Get all users with pagination.
  - **Parameters**: `Pageable` (page, size, sort)
  - **Response**: `PageUserDto` (totalPages, totalElements, size, content, number, sort, pageable, first, last, numberOfElements, empty)

- **GET /v1/users/{username}**
  - **Description**: Get a user by username.
  - **Parameters**: username (path)
  - **Response**: `UserDto` (id, firstname, lastname, username, email)

### Exchanger
- **GET /v1/exchanger**
  - **Description**: Get exchange rates.
  - **Response**: Array of `ExchangeRateDto` (id, code, name, rate, rateDate)

### Conversion History
- **GET /v1/conversion/history**
  - **Description**: Get conversion history with filters and pagination.
  - **Parameters**: from, to, username, dateRequest, `Pageable` (page, size, sort)
  - **Response**: `PageConversionDto` (totalPages, totalElements, size, content, number, sort, pageable, first, last, numberOfElements, empty)

### Refresh Token
- **GET /v1/auth/refresh-token**
  - **Description**: Create a new refresh token.
  - **Response**: Object with new token details.

### API Details
- **GET /v1/auth/api/details**
  - **Description**: Get API details.
  - **Response**: Object with API details.

## Schemas
- **ConversionRequest**: Contains currencyFrom, currencyTo, and fromValue.
- **ConversionDto**: Contains details of the conversion including user and creation date.
- **ExchangeRateDto**: Contains exchange rate details.
- **UserDto**: Contains user details.
- **RegisterRequest**: Contains registration details.
- **LoginRequest**: Contains login credentials.
- **LoginResponse**: Contains JWT token.
- **Pageable**: Contains pagination details.
- **PageUserDto**: Contains paginated user details.
- **PageConversionDto**: Contains paginated conversion details.

## Security Schemes
- **BearerAuth**: JWT based authentication.


## Built With

* Java 17
* Docker, Docker-Compose
* Spring Boot - The framework used
* Gradle - Dependency Management

## Authors

* **Arzuv Bahramov** - *innowise* - mr.arzuv
