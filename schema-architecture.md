# 🏗️ Section 1: Architecture Summary

The **Smart Clinic Management System** is a Spring Boot application designed using a **three-tier architecture**. It combines both **MVC** and **RESTful** design principles:

- **Thymeleaf templates** are used for server-rendered views (Admin and Doctor dashboards).
- **JSON-based REST APIs** serve Patient, Appointment, and related modules for frontend/mobile interaction.

The application interacts with two databases:

- **MySQL**: for structured relational data like Patients, Doctors, Appointments, and Admins.
- **MongoDB**: for flexible, document-based data such as Prescriptions.

All controllers (MVC & REST) delegate logic to a shared **Service Layer**, which:

- Implements business rules
- Coordinates between different modules
- Interacts with the **Repository Layer**, which abstracts database operations using:
  - **Spring Data JPA** for MySQL
  - **Spring Data MongoDB** for MongoDB

This modular design improves scalability, testing, and maintainability.

---

# 🔁 Section 2: Numbered Flow of Data and Control

1. **User Access**  
   - The user opens a Thymeleaf dashboard (Admin or Doctor)  
   - OR interacts via REST API (frontend/mobile app) for patients, appointments, etc.

2. **Request Routed to Controller**  
   - Server-side requests go to **MVC Controllers** → return `.html` views  
   - API requests go to **REST Controllers** → return JSON responses

3. **Controller Delegates to Service Layer**  
   - Controllers pass requests to service methods  
   - Logic like scheduling, validation, or fetching data is triggered

4. **Service Layer Coordinates Logic**  
   - Performs validations  
   - Manages workflows (e.g., doctor availability)  
   - Delegates DB interactions to repository interfaces

5. **Repositories Access Databases**  
   - `MySQL`: Spring Data JPA repositories query structured tables  
   - `MongoDB`: Spring Data MongoDB repositories handle document queries

6. **Data Retrieved and Bound to Models**  
   - MySQL data is mapped to `@Entity` classes  
   - MongoDB documents are mapped to `@Document` classes

7. **Response Constructed and Returned**  
   - **Thymeleaf**: model attributes are passed into HTML templates  
   - **REST API**: data is returned as JSON (DTOs or model objects)

---
