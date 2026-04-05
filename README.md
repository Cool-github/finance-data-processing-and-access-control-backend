# 💰 Finance Dashboard Backend

> **Assignment Submission**: Backend Developer Intern - Finance Data Processing and Access Control Backend

A production-ready **Spring Boot backend application** for managing personal financial records with **JWT authentication, role-based access control, dashboard analytics, and Docker support**.

---

## 📋 Table of Contents

- [Features](#-features)
- [Tech Stack](#-tech-stack)
- [Architecture](#️-architecture)
- [Project Structure](#️-project-structure)
- [Setup Prerequisites](#️-setup-prerequisites)
- [Getting Started](#-getting-started)
- [API Documentation Summary](#-api-documentation-summary)
- [Authentication Flow](#-authentication-flow)
- [User Management](#-user-management)
- [Financial Records APIs](#-financial-records-apis)
- [Dashboard API](#-dashboard-api)
- [Database Schema](#️-database-schema)
- [Assumptions & Trade-offs](#-assumptions--trade-offs)
- [Security & Access Control](#️-security--access-control)
- [Rate Limiting](#-rate-limiting)
- [Docker Configuration](#-docker-configuration)
- [Swagger Documentation](#-swagger-documentation)
- [Testing Flow](#-testing-flow)
- [Known Limitations & Future Enhancements](#-known-limitations--future-enhancements)

---

## 🎯 Features

- 🔐 **JWT Authentication** (Login-based)
- 👥 **Role-Based Access Control** (ADMIN, ANALYST, VIEWER)
- 🧾 **Financial Records Management** (CRUD)
- 📊 **Dashboard Analytics** (income, expense, summary)
- 🔍 **Filtering & Pagination**
- 🗑️ **Soft Delete** (no permanent deletion)
- 🧠 **Audit Fields** (createdBy, updatedBy)
- ⚡ **Rate Limiting** (Bucket4j)
- 🐳 **Dockerized Setup** (App + PostgreSQL)
- 📘 **Swagger API Documentation**

---

## 🧱 Tech Stack

- **Java 21**
- **Spring Boot 3.x**
- **Spring Security 6.x**
- **Spring Data JPA**
- **PostgreSQL**
- **JWT (io.jsonwebtoken)**
- **Bucket4j** (Rate Limiting)
- **Docker & Docker Compose**
- **Maven**
- **Lombok** (Boilerplate Reduction)
- **Swagger/OpenAPI 3.0**

---

## 🏗️ Architecture

### High-Level Flow
```
Client Request → Security Filter → JWT Validation → Role Authorization → Controller → Service → Repository → Database
```

### Layered Architecture
```
┌─────────────────────────────────────┐
│     Presentation Layer              │
│  (Controllers, DTOs, Exception)     │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│      Business Logic Layer           │
│        (Services, Validators)       │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│      Data Access Layer              │
│    (Repositories, Entities)         │
└─────────────────────────────────────┘
              ↓
┌─────────────────────────────────────┐
│         Database (PostgreSQL)       │
└─────────────────────────────────────┘
```

---

## 🗂️ Project Structure

```
com.finance.dashboard
│
├── config
│   ├── SecurityConfig.java              # JWT & Role-based security
│   ├── AuditorConfig.java               # JPA auditing config
│   └── RateLimitingFilter.java          # Bucket4j rate limiter
│
├── controller
│   ├── AuthController.java              # /auth/register, /auth/login
│   ├── UserController.java              # /users (CRUD)
│   └── FinancialRecordController.java   # /records (CRUD + Dashboard)
│
├── dto
│   ├── ApiResponse.java
│   ├── AuthRequestDTO.java
│   ├── AuthResponseDTO.java
│   ├── UserRequestDTO.java
│   ├── UserResponseDTO.java
│   ├── FinancialRecordRequestDTO.java
│   ├── FinancialRecordResponseDTO.java
│   └── DashboardResponseDTO.java
│
├── entity
│   ├── BaseEntity.java                  # Audit fields (createdBy, updatedBy)
│   ├── User.java
│   └── FinancialRecord.java
│
├── enums
│   ├── Role.java                        # ADMIN, ANALYST, VIEWER
│   ├── Category.java                    # SALARY, FOOD, TRANSPORT, etc.
│   ├── RecordType.java                  # INCOME, EXPENSE
│   └── UserStatus.java                  # ACTIVE, INACTIVE
│
├── exception
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── ResourceAlreadyExistsException.java
│   └── UnauthorizedException.java
│
├── repository
│   ├── UserRepository.java
│   └── FinancialRecordRepository.java
│
├── security
│   ├── JwtAuthFilter.java               # JWT validation filter
│   └── JwtService.java                  # Token generation & parsing
│
├── service
│   ├── AuthService.java
│   ├── UserService.java
│   ├── FinancialRecordService.java
│   └── impl
│       ├── AuthServiceImpl.java
│       ├── UserServiceImpl.java
│       └── FinancialRecordServiceImpl.java
│
└── FinanceDashboardApplication.java
```

---

## ⚙️ Setup Prerequisites

Before running the project, ensure the following are installed:

- ✅ **Java 21** or higher
- ✅ **Maven 3.8+**
- ✅ **Docker & Docker Compose**
- ✅ **Git**
- ✅ (Optional) **Postman** / **Swagger UI** for API testing

### 🔹 Verify Installation

```bash
java --version
mvn -version
docker --version
docker-compose --version
```

---

## 🚀 Getting Started

### 🔹 Option 1: Run with Docker (Recommended)

```bash
# Clone the repository
git clone https://github.com/yourusername/finance-dashboard-backend.git
cd finance-dashboard-backend

# Start services
docker-compose up --build
```

**Application runs at:**
```
http://localhost:8081
```

**Swagger UI:**
```
http://localhost:8081/swagger-ui/index.html
```

---

### 🔹 Option 2: Run Locally

```bash
# Install dependencies
mvn clean install

# Run application
mvn spring-boot:run
```

> **Note:** Update `application.yml` with your local PostgreSQL credentials

---

## 📘 API Documentation Summary

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| POST | `/auth/register` | Bootstrap first admin | Public (only once) |
| POST | `/auth/login` | Login and get JWT token | Public |
| POST | `/users` | Create new user | ADMIN |
| GET | `/users` | Get all users | ADMIN, ANALYST |
| POST | `/records` | Create financial record | Authenticated |
| GET | `/records` | Get records (pagination + filter) | Authenticated |
| PUT | `/records/{id}` | Update record | Owner only |
| DELETE | `/records/{id}` | Soft delete record | Owner only |
| GET | `/records/dashboard` | Dashboard analytics | Authenticated |

---

### 🔐 Access Control Summary

| Role | Permissions |
|------|-------------|
| **ADMIN** | Full access (user management + records) |
| **ANALYST** | Can view users, manage own records |
| **VIEWER** | Only manage/view own records |

---

## 🔐 Authentication Flow

### 1️⃣ Register First Admin (Bootstrap)

**Endpoint:**
```
POST /auth/register
```

**Request Body:**
```json
{
  "name": "Admin",
  "email": "admin@example.com",
  "password": "1234"
}
```

> **⚠️ Important:**
> - Only works **once**
> - Creates the first **ADMIN**
> - Further registration is **disabled**

---

### 2️⃣ Login

**Endpoint:**
```
POST /auth/login
```

**Request Body:**
```json
{
  "email": "admin@example.com",
  "password": "1234"
}
```

**Response:**
```json
{
  "token": "JWT_TOKEN"
}
```

---

### 3️⃣ Use Token

Add header to all requests:

```
Authorization: Bearer <JWT_TOKEN>
```

---

## 👥 User Management

### Create User (ADMIN only)

**Endpoint:**
```
POST /users
```

**Request Body:**
```json
{
  "name": "Analyst",
  "email": "analyst@example.com",
  "password": "1234",
  "role": "ROLE_ANALYST"
}
```

---

## 💰 Financial Records APIs

### Create Record

**Endpoint:**
```
POST /records
```

**Request Body:**
```json
{
  "amount": 5000,
  "type": "INCOME",
  "category": "SALARY",
  "date": "2024-04-01",
  "description": "Salary"
}
```

---

### Get Records (Pagination + Filter)

**Endpoint:**
```
GET /records?page=0&size=10&type=EXPENSE&category=FOOD
```

---

### Update Record

**Endpoint:**
```
PUT /records/{id}
```

---

### Delete Record (Soft Delete)

**Endpoint:**
```
DELETE /records/{id}
```

---

## 📊 Dashboard API

**Endpoint:**
```
GET /records/dashboard
```

**Returns:**
- ✅ Total Income
- ✅ Total Expense
- ✅ Net Balance
- ✅ Category-wise Summary
- ✅ Last 5 Transactions

---

## 🗄️ Database Schema

### Entity Relationship Diagram

```
┌─────────────────────┐
│       User          │
├─────────────────────┤
│ id (UUID, PK)       │
│ name                │
│ email (unique)      │
│ password (hashed)   │
│ role (enum)         │
│ status (enum)       │
│ created_at          │
│ updated_at          │
│ created_by          │
│ updated_by          │
│ is_deleted          │
│ deleted_at          │
└─────────────────────┘
          │
          │ 1:N
          ▼
┌─────────────────────┐
│  FinancialRecord    │
├─────────────────────┤
│ id (UUID, PK)       │
│ amount              │
│ type (enum)         │
│ category (enum)     │
│ date                │
│ description         │
│ user_id (FK)        │
│ created_at          │
│ updated_at          │
│ created_by          │
│ updated_by          │
│ is_deleted          │
│ deleted_at          │
└─────────────────────┘
```

### Key Database Features

- **UUID Primary Keys:** Prevents ID enumeration attacks, better for distributed systems
- **Soft Delete:** `is_deleted` and `deleted_at` flags prevent permanent data loss
- **Audit Trail:** `created_by`, `updated_by`, `created_at`, `updated_at` tracked automatically
- **User Isolation:** Users can only access their own financial records
- **Indexes:** Email (unique), user_id + is_deleted (for efficient queries)

---

## 📌 Assumptions & Trade-offs

### 🔹 Technology Choices

- **Spring Boot**: Chosen for rapid development, strong ecosystem, and production-ready features like security, JPA, and dependency management.
- **PostgreSQL**: Selected for reliability, ACID compliance, and strong support for relational data (suitable for financial records).
- **JWT Authentication**: Enables stateless, scalable authentication without server-side session storage.
- **Bucket4j**: Used for lightweight, in-memory rate limiting to prevent API abuse.
- **Docker**: Ensures consistent environment setup and easy project portability.

---

### 🔹 Database Schema Decisions

- **UUID as Primary Key**:
    - Prevents ID enumeration attacks
    - Better for distributed systems

- **Separate User & FinancialRecord Tables**:
    - Clear separation of concerns
    - Supports multi-user data isolation

- **Soft Delete (`is_deleted`, `deleted_at`)**:
    - Prevents permanent loss of financial data
    - Enables audit/history tracking

- **Audit Fields (`created_at`, `updated_at`, `created_by`, `updated_by`)**:
    - Provides traceability for all operations

---

### 🔹 Authentication & Authorization Design

- **JWT-based Authentication**:
    - Stateless → no server memory overhead
    - Suitable for scalable and distributed systems

- **Role-Based Access Control (RBAC)**:
    - `ADMIN`: Full control (user management)
    - `ANALYST`: Can view users and manage own records
    - `VIEWER`: Only manage/view own records

- **Bootstrap Admin Registration**:
    - Only one admin can be created via `/auth/register`
    - Prevents system lock in fresh deployments
    - After that, all users are created by admin only

---

### 🔹 Assumptions Made

- Only one **ADMIN** exists initially and manages the system
- Users can only access their own financial data (strict isolation)
- No public signup after admin creation (controlled system)
- Financial records should never be permanently deleted (soft delete applied)
- API consumers (frontend/Postman) will handle JWT storage and reuse
- Rate limiting is applied per IP (not per user)

---

### 🔹 Trade-offs

| Decision | Benefit | Trade-off |
|----------|---------|-----------|
| **In-memory rate limiting** | Simple and fast | Not distributed (resets on restart) |
| **No refresh tokens** | Simpler implementation | Requires re-login after token expiry |
| **Single admin bootstrap** | Secure and controlled | Less flexible for open systems |
| **`ddl-auto: update` in development** | Faster iteration | Not recommended for production (should use migrations like Flyway) |
| **CORS allow all (development)** | Easier testing | Should be restricted in production |

---

## 🛡️ Security Highlights

- ✅ **JWT-based authentication**
- ✅ **BCrypt password hashing**
- ✅ **Role-based authorization**
- ✅ **Rate limiting protection**
- ✅ **No public signup** after admin creation
- ✅ **UUID primary keys** (prevents enumeration)
- ✅ **Soft delete** (data preservation)
- ✅ **User data isolation**

---

## ⚡ Rate Limiting

- **10 requests per minute** per IP
- Returns `429 Too Many Requests` if exceeded
- Implementation: Bucket4j with in-memory storage
- Future: Redis-backed distributed rate limiting

---

## 🐳 Docker Configuration

### Services
- **Spring Boot App**
- **PostgreSQL Database**

### Environment Variables

```bash
SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/finance_db
SPRING_DATASOURCE_USERNAME=financeuser
SPRING_DATASOURCE_PASSWORD=securepassword
JWT_SECRET=your-256-bit-secret-key
JWT_EXPIRATION=3600000
```

---

## 📘 Swagger Documentation

Access API docs:

```
http://localhost:8081/swagger-ui/index.html
```

---

## 🧪 Testing Flow (Quick)

1. **Start app** (Docker)
2. **Register admin**
3. **Login** → get token
4. **Create users**
5. **Add financial records**
6. **View dashboard**

> **Note:** Unit tests are not implemented in the current version. Future enhancement planned.

---

## 🚨 Known Limitations & Future Enhancements

### Current Limitations

- ❌ No refresh token mechanism (tokens can't be revoked before expiry)
- ❌ Email verification not implemented
- ❌ Password reset functionality missing
- ❌ Rate limiting is IP-based (can be bypassed with VPN)
- ❌ Unit/Integration tests not included
- ❌ No data export (CSV/PDF) functionality

---

### 📌 Future Enhancements

- [ ] Refresh tokens
- [ ] Email verification
- [ ] Password reset flow
- [ ] Role hierarchy improvements
- [ ] Redis-based rate limiting
- [ ] CI/CD pipeline
- [ ] Comprehensive test coverage (Unit + Integration)
- [ ] Data export features (CSV/PDF)
- [ ] Advanced search and filtering
- [ ] Monitoring and logging (Prometheus + Grafana)

---

## 🤝 Contributing

This is an assignment submission. For feedback or suggestions:

📧 **Email:** gopi.lucky8066@gmail.com  
🔗 **LinkedIn:** linkedin.com/in/gopi-chand-bandikatla  
💻 **GitHub:** https://github.com/Cool-github
