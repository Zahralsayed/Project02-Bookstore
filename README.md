# Bookstore App

A Java Spring Boot Bookstore application that allows users to browse books, place orders, and manage their profiles. The app includes **JWT-based authentication** for secure login and role-based access control, supporting both customers and admins. Admins can manage users, books, and orders, while customers can view and purchase books.

---

## Project Description

This Bookstore App is designed to simulate a real-world online bookstore. Key features include:

- **User Authentication** with JWT  
- **Role-based access control** (Customer vs Admin)  
- **Order management** with multiple books per order  
- **User profile management** with profile image, address, and contact details  
- **Book management** with categories, availability, and pricing

---

## Technologies Used

- **Backend:** Java, Spring Boot, Spring Security, Hibernate/JPA  
- **Database:** PostgreSQL  
- **Authentication:** JWT (JSON Web Token)  
- **Build Tools:** Maven / Gradle  
- **Version Control:** Git, GitHub  
- **Other Tools:** Postman, DBML (for ERD)

---

## User Stories

[View User Stories](https://trello.com/invite/b/6952b94c349dcac14cf77eb7/ATTI476b3e5d4d51b4843a78e7f248466357441D3886/bookstore)

---

## ERD Diagram

![ERD Diagram](https://github.com/user-attachments/assets/8c95a661-e90c-49b0-bac0-f6da4c0183d1)

> ERD shows entities: Users, UserProfiles, Books, Categories, Orders, OrderItems.  
> Relationships reflect real-world interactions:  
> ```
> User 1 ──── 1 UserProfile
> User 1 ──── * Orders
> Orders 1 ──── * OrderItem
> Book 1 ──── * OrderItem
> Category 1 ──── * Book
> ```

---

## JWT Authentication Flow

![JWT Flow](https://github.com/user-attachments/assets/3518b924-2e17-4f60-91e4-29b0c2db1efb)

> Flow Overview:
> 1. User logs in with username and password  
> 2. Server authenticates and issues a JWT token  
> 3. Token sent with every API request in `Authorization` header  
> 4. Server validates JWT and allows access based on user role  

---

## REST API Endpoints

| Request Type | URL                                | Functionality                           | Access  |
|--------------|------------------------------------|----------------------------------------|---------|
| POST         | /auth/users/register               | User registration                       | Public  |
| POST         | /auth/users/login                  | User login                               | Public  |
| GET          | /auth/users/verify                 | Verify user email                        | Public  |
| POST         | /auth/users/forgot-password        | Request password reset link              | Public  |
| POST         | /auth/users/reset-password         | Reset password using token               | Public  |
| POST         | /auth/users/change-password        | Change logged-in user password           | Private |
| GET          | /auth/users/getAllUsers            | Get all users                             | Admin   |
| PATCH        | /auth/users/status/{userId}       | Update user status                        | Admin   |
| DELETE       | /auth/users/delete/{userId}       | Soft delete user (status = INACTIVE)     | Admin   |
| POST         | /api/user-profiles/upload-image   | Upload profile image                      | Private |
| GET          | /api/user-profiles/images/{filename} | Get profile image by filename          | Public  |
| PUT          | /api/user-profiles/update-profile | Update full user profile                  | Private |
| GET          | /api/user-profiles/all-profiles   | Get all user profiles                      | Admin   |

