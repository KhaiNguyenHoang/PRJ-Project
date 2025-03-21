<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Books" %>
<%@ page import="model.Members" %>
<%@ page import="dao.BooksDAO" %>
<%@ page import="dao.MembersDAO" %>
<%@ page import="dao.TransactionDAO" %>
<%@ page import="dao.FineDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>
    <style>
        body {
            font-family: 'Arial', sans-serif;
            background-color: #f4f7fa;
            color: #333;
        }

        .container {
            margin-top: 30px;
        }

        .card {
            border: none;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        .card-header {
            background-color: #3498db;
            color: white;
            font-size: 1.25rem;
            font-weight: bold;
            border-radius: 8px 8px 0 0;
        }

        .card-body {
            padding: 30px;
            background-color: #fff;
        }

        .btn-custom {
            font-size: 1.1rem;
            padding: 12px 25px;
            margin-bottom: 15px;
            width: 100%;
            text-align: left;
            border-radius: 6px;
            transition: background-color 0.3s ease;
        }

        .btn-custom i {
            font-size: 1.3rem;
        }

        .btn-custom:hover {
            background-color: #2980b9;
            color: white;
        }

        .card-body a {
            display: block;
        }

        .card-body a:last-child {
            margin-bottom: 0;
        }

        .section-title {
            font-size: 1.5rem;
            font-weight: bold;
            color: #2c3e50;
            margin-bottom: 20px;
        }

        .card-footer {
            background-color: #ecf0f1;
            text-align: center;
            font-size: 0.9rem;
            padding: 15px;
        }

        .card-footer a {
            color: #2980b9;
            text-decoration: none;
        }

        .card-footer a:hover {
            text-decoration: underline;
        }

    </style>
</head>
<body>

<div class="container">
    <div class="row">
        <div class="col-md-12">
            <h1 class="text-center mb-5">Staff Dashboard</h1>

            <!-- Book Management Section -->
            <div class="card">
                <div class="card-header">
                    <h4><i class="fas fa-book me-2"></i> Book Management</h4>
                </div>
                <div class="card-body">
                    <a href="AddBook.jsp" class="btn btn-primary btn-custom">
                        <i class="fas fa-plus me-2"></i> Add New Book
                    </a>
                    <a href="ManageBooks.jsp" class="btn btn-success btn-custom">
                        <i class="fas fa-edit me-2"></i> Manage Existing Books
                    </a>
                    <a href="DeleteBook.jsp" class="btn btn-danger btn-custom">
                        <i class="fas fa-trash-alt me-2"></i> Delete Book
                    </a>
                </div>
            </div>

            <!-- Member Management Section -->
            <div class="card">
                <div class="card-header">
                    <h4><i class="fas fa-users me-2"></i> Member Management</h4>
                </div>
                <div class="card-body">
                    <a href="BanMember.jsp" class="btn btn-warning btn-custom">
                        <i class="fas fa-ban me-2"></i> Ban Member
                    </a>
                    <a href="UnbanMember.jsp" class="btn btn-info btn-custom">
                        <i class="fas fa-check-circle me-2"></i> Unban Member
                    </a>
                    <a href="UpdateMemberInfo.jsp" class="btn btn-secondary btn-custom">
                        <i class="fas fa-user-edit me-2"></i> Update Member Information
                    </a>
                </div>
            </div>

            <!-- Borrowing History Section -->
            <div class="card">
                <div class="card-header">
                    <h4><i class="fas fa-history me-2"></i> Borrowing History</h4>
                </div>
                <div class="card-body">
                    <a href="BorrowingHistory.jsp" class="btn btn-info btn-custom">
                        <i class="fas fa-history me-2"></i> View Borrowing History
                    </a>
                </div>
            </div>

            <!-- Fine and Payment Section -->
            <div class="card">
                <div class="card-header">
                    <h4><i class="fas fa-money-bill-alt me-2"></i> Fine & Payment</h4>
                </div>
                <div class="card-body">
                    <a href="ManageFines.jsp" class="btn btn-danger btn-custom">
                        <i class="fas fa-money-check-alt me-2"></i> Manage Fines
                    </a>
                    <a href="ViewPayments.jsp" class="btn btn-success btn-custom">
                        <i class="fas fa-credit-card me-2"></i> View Payments
                    </a>
                </div>
            </div>

        </div>
    </div>
</div>

<!-- Footer -->
<div class="card-footer">
    <p>© 2025 Library System | <a href="#">Privacy Policy</a></p>
</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
