# Enviro365 Investments Portal

## Description
A full-stack automated withdrawal notice system built for Enviro365 Investments to eliminate manual errors, improve efficiency, and deliver a better investor experience. 

## Tech Stack
- **Backend**: Java 21, Spring Boot 3.3.0, Spring Data JPA, H2 Database (In-Memory)
- **Frontend**: React, TypeScript, Vite
- **Architecture**: REST API, Clean Architecture principles

## Setup Instructions
### Prerequisites
- Java 21
- Node.js & npm

### Backend Setup
1. Navigate to the `backend` directory.
2. Run the application using the Maven wrapper:
   `./mvnw spring-boot:run`
3. The backend runs on `http://localhost:8080`.
4. The H2 Database console is accessible at `http://localhost:8080/h2-console`. 
   - **JDBC URL:** `jdbc:h2:mem:envirodb`
   - **User:** `sa`
   - **Password:** `password`

### Frontend Setup
1. Navigate to the `frontend` directory.
2. Install dependencies:
   `npm install`
3. Start the Vite development server:
   `npm run dev`
4. The application will be accessible at `http://localhost:5173`.

## API Documentation
The API follows REST best practices and exposes the following primary endpoints.

### Investors & Portfolios
- `GET /api/v1/investors/{id}/portfolio` 
  - Retrieves the investor's details and their associated product balances.

### Withdrawals
- `POST /api/v1/withdrawals` 
  - Creates a withdrawal notice.
  - **Business Rules Enforced:**
    - Retirement withdrawals are only allowed if the investor's age is > 65.
    - Withdrawal amount cannot exceed the total balance.
    - Withdrawal amount cannot exceed 90% of the current balance.
- `GET /api/v1/withdrawals/export` 
  - Exports a CSV statement of withdrawal history with filtering options.

