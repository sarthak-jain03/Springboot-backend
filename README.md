# Financial Records Management System

A Spring Boot backend application for managing financial records with
role-based access control, JWT authentication, rate limiting, and
analytics dashboards.

------------------------------------------------------------------------
### API Documentation Link: https://springboot-backend-oizc.onrender.com/swagger-ui/index.html
------------------------------------------------------------------------

## Features

### Authentication & Authorization

-   JWT-based authentication
-   Role-based access:
    -   VIEWER → limited access
    -   ANALYST → read + analytics
    -   ADMIN → full control

------------------------------------------------------------------------

### User Management (Admin Only)

-   Create users
-   Update roles (PATCH /roles)
-   Update status (PATCH /status)
-   Delete users
-   Pagination & sorting

------------------------------------------------------------------------

### Financial Records

-   Create records (Admin)
-   View records (Analyst/Admin)
-   Update records
-   Soft delete records
-   Filtering:
    -   by type (INCOME / EXPENSE)
    -   by category
    -   by date range

------------------------------------------------------------------------

### Dashboard & Analytics

-   Total income & expenses
-   Category-wise summary
-   Monthly trends
-   Recent activity

------------------------------------------------------------------------

### Rate Limiting

-   Limits requests per IP
-   Returns 429 Too Many Requests

------------------------------------------------------------------------

### Exception Handling

-   Global exception handler
-   Custom exceptions:
    -   UserNotFoundException
    -   UserAlreadyExistsException
    -   UserInactiveException
    -   RecordNotFoundException

------------------------------------------------------------------------

## Tech Stack

-   Spring Boot
-   Spring Security + JWT
-   MySQL
-   Spring Data JPA
-   Swagger (OpenAPI)
-   Lombok

------------------------------------------------------------------------

## API Endpoints

### Auth

POST /api/auth/signup\
POST /api/auth/login

### Users

GET /api/users\
GET /api/users/{id}\
PATCH /api/users/{id}/roles\
PATCH /api/users/{id}/status\
DELETE /api/users/{id}

### Records

POST /api/records\
GET /api/records\
GET /api/records/{id}\
PUT /api/records/{id}\
DELETE /api/records/{id}

### Dashboard

GET /api/dashboard/summary\
GET /api/dashboard/category-summary\
GET /api/dashboard/monthly-trend\
GET /api/dashboard/recent-activity

------------------------------------------------------------------------
### API Documentation Assests
![Swagger UI](/assests/img.png)
![Swagger UI](/assests/img_1.png)
![Swagger UI](/assests/img_2.png)
![Swagger UI](/assests/img_3.png)


### Postman Response
![Swagger UI](/assests/img_4.png)

------------------------------------------------------------------------

## Setup

1.  Clone repo
2.  Configure MySQL in application.properties
3.  Run: mvn spring-boot:run
4.  Swagger: http://localhost:8080/swagger-ui/index.html

------------------------------------------------------------------------

## Author

Name: Sarthak Jain\
Email: sarthakjain4452@gmail.com
