# PRJ Project: Library Management Website use SQL Server

A web-based library management system that allows staff to manage books, members, borrowing, and fines.

## Features

- Member Management
  - Register new members
  - Update member information
  - Ban/unban members
  - Manage member accounts

- Book Management
  - Add new books
  - Delete books
  - Update book information
  - Search books

- Borrowing System
  - Borrow books
  - Return books
  - View borrowing history

- Fine Management
  - Calculate fines
  - Process fine payments
  - Manage fine records

- Staff Management
  - Add staff accounts
  - Manage staff permissions
  - Staff authentication

## Technology Stack

- Java Web Application (JSP/Servlet)
- SQL Server Database
- Bootstrap for frontend
- Maven for dependency management

## Project Structure

```
src/
├── main/
    ├── java/
    │   ├── controller/    # Servlet controllers
    │   ├── dao/          # Data access objects
    │   ├── model/        # Entity classes
    │   └── utils/        # Utility classes
    └── webapp/
        ├── Auth/         # Authentication pages
        ├── HomeHTML/     # Main application pages
        └── WEB-INF/      # Web configuration
```

## Database

The system uses SQL Server for data storage, managing tables for:
- Members
- Books
- Book Copies
- Borrowing Records
- Fines
- Staff Accounts