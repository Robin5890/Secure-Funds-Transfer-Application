# Secure Funds Transfer Application
## Project Overview
A full-stack web application that allows users to securely log in and transfer funds between accounts. The frontend is built with **Angular** and the backend with **Java Spring Boot**.
Users can view their balance and perform fund transfers securely using **JWT(JSON web tokens)** authentication.


## Live Demo
https://secure-funds-transfer-application.vercel.app

>**Note**: The first request to the backend will take a while(30+ seconds) to respond due to Render's free tier cold start policy. Subsequent requests will be much quicker.


## Sample Credentials(for testing)
User 1: username: alice, password: password123, account number: ACC1001
User 2: username: bob, password: mypassword, account number: ACC1002


## Technologies used
-**Frontend**: Angular.
-**Backend**: Java Spring Boot, Spring Security, Hibernate.
-**Authentication:** JWT, BCrypt password hashing.
-**Deployement**:
    - Backend: Render.   
    - Frontend: Vercel. 
    - Database: Neon(PostgreSQL)



## Local Setup Instructions

- Backend:

1- Go to pom.xml and synchronize the java dependencies.
2- Go to application.settings(in resources folder) and set the database connection variables to a local database of choice.(e.g jdbc:mysql://localhost:3306/mydatabase)
3- In the SecurityConfig.java file change the corsConfigurationSource method and set the config.setAllowedOrigins to "http://localhost:4200"(or whatever port the frontend runs on)
4- Run SftaApplication.java


- Frontend:
1- Change apiUrl in environment.development.ts to "http://localhost:8080"(or the port of the backend)
2- Open a terminal in frontend/sfta-frontend.
3- Run:
  npm install
  ng serve


-Database:
SQL(Neon uses PostgreSQL): Run the following SQL commands to create tables and insert sample data.

CREATE TABLE users (
    id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username varchar(50) NOT NULL UNIQUE,
    password varchar(255) NOT NULL
);

CREATE TABLE accounts (
    id integer PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    account_number varchar(20) NOT NULL UNIQUE,
    balance numeric(12,2) DEFAULT 0 NOT NULL,
    user_id integer NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES users(id)
);

INSERT INTO users (username, password)
VALUES 
('alice', '$2a$10$v66fZiyaO/IFmc7dNFpYROxvdl8TvqiBUwebqRItcS8jR2Tn8ZgGi'),  -- BCrypt hashed "password123"
('bob', '$2a$10$.lxvYW/qp1WPTBTsknJmiO6JvhJ/9lnrUHwN/vrj4UnFyb6bYLSuC');   -- BCrypt hashed "mypassword"


INSERT INTO accounts (account_number, balance, user_id)
VALUES 
('ACC1001', 5000.00, 1),
('ACC1002', 3000.00, 2);




## API Endpoints
- /login : POST endpoint that authenticates a user generates and returns a JWT.
- /getUserData: Get endpoint that authenticates a user with a valid JWT and returns their information(username and balance)
- /transfer: POST endpoint that authenticates a user with a valid JWT, verifies the existance of sender and receiver accounts and commits the transfer of balance between accounts with proper validation.


