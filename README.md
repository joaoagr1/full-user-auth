# Authentication Module

## Overview

This project is an authentication module built with Java and Spring Boot. It provides functionalities for users registration, login, password management, and email verification.

## Table of Contents

- [Getting Started](#getting-started)
- [Project Structure](#project-structure)
- [Endpoints](#endpoints)
- [DTOs](#dtos)
- [Services](#services)
- [Repositories](#repositories)
- [Security](#security)
- [Configuration](#configuration)

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6.0 or higher
- A running instance of a database (e.g., PostgreSQL)

### Installation

1. Clone the repository:
   ```sh
   git clone https://github.com/yourusername/authentication-module.git
   cd authentication-module
   ```

2. Configure the database connection in `src/main/resources/application.properties`:
   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
   spring.datasource.username=yourusername
   spring.datasource.password=yourpassword
   ```

3. Build the project:
   ```sh
   mvn clean install
   ```

4. Run the application:
   ```sh
   mvn spring-boot:run
   ```

## Project Structure

```plaintext
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── authentication/
│   │           ├── controllers/
│   │           ├── domain/
│   │           ├── dtos/
│   │           ├── exceptions/
│   │           ├── repositories/
│   │           ├── services/
│   │           └── AuthenticationModuleApplication.java
│   └── resources/
│       ├── application.properties
│       └── templates/
└── test/
    └── java/
        └── com/
            └── authentication/
```

## Endpoints http://localhost:8585/swagger-ui/index.html#/

![image](https://github.com/user-attachments/assets/ce2f16c7-68a8-457d-9283-a19db02243da)

## DTOs

### LoginRequestDTO

```java
public record LoginRequestDTO(String login, String password) {}
```

### LoginResponseDTO

```java
public record LoginResponseDTO(String token) {}
```

### RegisterDTO

```java
public record RegisterDTO(String login, String password, String role, String document, String email) {}
```

### SuccessResponseDTO

```java
public record SuccessResponseDTO(String message) {}
```

### ResetPasswordDTO

```java
public record ResetPasswordDTO(String token, String newPassword) {}
```

### UpdatePasswordDTO

```java
public record UpdatePasswordDTO(String oldPassword, String newPassword, String confirmNewPassword) {}
```

## Services

### LoginService

Handles users authentication, password reset, and token generation.

### RegisterService

Handles users registration and email verification.

### UserService

Handles users management, including password updates and users deletion.

### TokenService

Handles JWT token generation and validation.

## Repositories

### UserRepository

```java
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByLogin(String login);
    Optional<User> findByEmail(String email);
    boolean existsByLogin(String login);
    boolean existsByDocument(String document);
    boolean existsByEmail(String email);
}
```

### LoginRepository

```java
public interface LoginRepository extends JpaRepository<Login, Long> {}
```

### PasswordResetTokenRepository

```java
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String token);
}
```

## Security

### Password Encoding

Passwords are encoded using `BCryptPasswordEncoder`.

### JWT

JWT tokens are used for authentication. Tokens are generated and validated using the `TokenService`.

## Configuration

### application.properties

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/yourdatabase
spring.datasource.username=yourusername
spring.datasource.password=yourpassword
api.security.token.secret=your-secret-key
```

## License

This project is licensed under the MIT License. See the `LICENSE` file for details.
