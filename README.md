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
2. The tables will be automatically generated upon first run by Hibernate.

### 🧹 Database Maintenance (MySQL Workbench)
If you need to reset your database and delete all existing data/tables, run the following script in MySQL Workbench:

```sql
-- Step 1: Disable foreign key checks to allow dropping tables with dependencies
SET FOREIGN_KEY_CHECKS = 0;

-- Step 2: Drop all known tables
DROP TABLE IF EXISTS brands;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS enquiries;
DROP TABLE IF EXISTS news;
DROP TABLE IF EXISTS password_reset_token;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS refresh_token;
DROP TABLE IF EXISTS seller_profile;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS wishlist;
DROP TABLE IF EXISTS product_images; -- Hibernate generated join table if applicable

-- Step 3: Re-enable foreign key checks
SET FOREIGN_KEY_CHECKS = 1;

-- OPTIONAL: Hard Reset (Drops and Recreates the entire database)
-- DROP DATABASE dealhub99_db;
-- CREATE DATABASE dealhub99_db;
```

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

### 🔑 Admin Access
The system initializes a master admin account by default for initial setup:
- **Admin Login URL (Frontend):** `http://localhost:5173/admin/login`
- **Email:** `admin@dealhub99.com`
- **Password:** `admin123`

### 🔓 Public Endpoints
| Endpoint | Method | Description | Body |
| :--- | :--- | :--- | :--- |
| `/api/auth/register` | `POST` | Create a new Buyer or Seller account | `{fullName, email, mobileNumber, password, role}` |
| `/api/auth/login` | `POST` | Authenticate and receive a JWT token | `{email, password}` |
| `/api/auth/forgot-password` | `POST` | Request a password reset link | `{email}` |
| `/api/auth/reset-password` | `POST` | Finalize password change with token | `{token, newPassword}` |

### 🔒 Admin-Only Endpoints
These endpoints require a JWT token with the `ADMIN` role.
| Endpoint | Method | Description |
| :--- | :--- | :--- |
| `/api/admin/dashboard` | `GET` | Administrative Dashboard |
| `/api/admin/users` | `GET` | Fetch all registered users |
| `/api/admin/sellers` | `GET` | Fetch all registered sellers |

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
