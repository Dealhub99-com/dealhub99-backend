# DealHub99 Authentication Backend

A robust, production-ready Authentication and Security module for the **DealHub99** ecosystem. Built with Spring Boot 3, Spring Security, and JWT for secure user management.

## 🚀 Technologies Used
- **Core:** Java 17, Spring Boot 3.4.2
- **Security:** Spring Security, JSON Web Token (JWT)
- **Database:** MySQL, Spring Data JPA, Hibernate
- **Utilities:** Lombok, Jakarta Validation
- **Mail:** Spring Boot Starter Mail (SMTP)

## ✨ Core Features
- **User Registration:** Secure onboarding with role management (BUYER/SELLER/ADMIN).
- **JWT-Based Login:** Stateless authentication return dynamic JWT tokens.
- **Forgot/Reset Password:** Fully automated workflow with random token generation and expiry (15 mins).
- **Secure Password Hashing:** BCrypt implementation for all credential storage.
- **Global Exception Handling:** Consistent API responses with proper HTTP status codes.

---

## 🛠️ Getting Started

### Prerequisites
- **Java 17** or higher
- **Maven 3.6+**
- **MySQL 8.0+**

### Database Setup
1. Create a MySQL database (matching `application.properties`):
   ```sql
   CREATE DATABASE dealhub99_db;
   ```
2. The tables (`users` and `password_reset_token`) will be automatically generated upon first run.

### Configuration
Update `src/main/resources/application.properties` with your environment-specific credentials:
- Database URL, Username, and Password.
- `dealhub99.app.jwtSecret`: A long, unique key for token signing.
- `spring.mail.*`: SMTP details for password reset emails.

### Run the Application
```bash
./mvnw clean spring-boot:run
```

---

## 🔑 API Documentation

### 🔓 Public Endpoints
| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/api/auth/register` | `POST` | Create a new Buyer or Seller account |
| `/api/auth/login` | `POST` | Authenticate and receive a JWT token |
| `/api/auth/forgot-password` | `POST` | Request a password reset link |
| `/api/auth/reset-password` | `POST` | Finalize password change with token |

### 🔒 Admin-Only Endpoints
| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/api/admin/dashboard` | `GET` | Administrative Dashboard (Requires `ADMIN` role) |

### 🔒 Protected Endpoints
All other API routes are protected by default. Include the JWT in your headers:
```http
Authorization: Bearer <your_jwt_token>
```

---

## 📋 Response Structure
Every API response follows a unified format:
```json
{
    "status": "SUCCESS / ERROR",
    "message": "Human-readable description",
    "data": { ... }
}
```

## 🤝 Project Structure
- `controller`: API route handlers.
- `service`: Business logic orchestration.
- `repository`: JPA Data access layers.
- `entity`: Database models.
- `dto`: Request and Response mapping objects.
- `security`: JWT filter, entry points, and security configurations.
- `exception`: Global advice and custom exception classes.
