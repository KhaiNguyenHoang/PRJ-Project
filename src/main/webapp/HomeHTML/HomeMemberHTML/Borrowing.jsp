<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="model.entity.Books, model.entity.BorrowingHistory, java.util.List, java.text.SimpleDateFormat" %>
<%@ page import="model.dao.BooksDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý mượn sách</title>
    <!-- Owl Carousel -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/assets/owl.carousel.min.css"
          rel="stylesheet"/>
    <!-- Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,700&display=swap" rel="stylesheet"/>
    <!-- Bootstrap CSS -->
    <link href="HomeHTML/HomeMemberHTML/css/bootstrap.css" rel="stylesheet"/>
    <!-- Custom CSS -->
    <link href="HomeHTML/HomeMemberHTML/css/style.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/responsive.css" rel="stylesheet"/>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"/>
    <style>
        .card {
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
            border-radius: 15px;
            padding: 25px;
            margin-top: 30px;
            background-color: #fff;
            transition: transform 0.3s;
        }

        .card:hover {
            transform: translateY(-5px);
        }

        .card-header {
            font-size: 1.8rem;
            font-weight: bold;
            color: #2c3e50;
            border-bottom: 2px solid #007bff;
            padding-bottom: 10px;
        }

        .table th {
            background-color: #007bff;
            color: white;
            text-align: center;
        }

        .table td {
            vertical-align: middle;
            text-align: center;
        }

        .btn-primary {
            background-color: #007bff;
            border-color: #007bff;
            border-radius: 8px;
            padding: 10px 25px;
            transition: background-color 0.3s;
        }

        .btn-primary:hover {
            background-color: #0056b3;
        }

        .search-form {
            display: flex;
            align-items: center;
            margin-bottom: 20px;
        }

        .search-form input {
            padding: 12px;
            border-radius: 8px;
            border: 1px solid #ccc;
            flex-grow: 1;
        }

        .search-form button {
            padding: 12px 25px;
            border-radius: 8px;
            background-color: #28a745;
            color: white;
            border: none;
            margin-left: 15px;
            transition: background-color 0.3s;
        }

        .search-form button:hover {
            background-color: #218838;
        }

        .book-item {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 15px;
            border-bottom: 1px solid #eee;
        }

        .book-item .btn {
            font-size: 14px;
            padding: 8px 15px;
        }

        .footer_section {
            background-color: #2c3e50;
            color: white;
            text-align: center;
            padding: 20px 0;
            margin-top: 40px;
        }

        .alert {
            border-radius: 8px;
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<!-- Header -->
<header class="header_section">
    <div class="container-fluid">
        <nav class="navbar navbar-expand-lg custom_nav-container">
            <a class="navbar-brand" href="index.jsp"><span>Library</span></a>
            <button class="navbar-toggler" type="button" data-bs-toggle="collapse"
                    data-bs-target="#navbarSupportedContent"
                    aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ms-auto">
                    <li class="nav-item"><a class="nav-link" href="HomePage">Home</a></li>
                    <li class="nav-item"><a class="nav-link" href="#about_section">About</a></li>
                    <li class="nav-item"><a class="nav-link" href="#how_section">How</a></li>
                    <li class="nav-item active"><a class="nav-link" href="Borrowing">Borrowing</a></li>
                    <li class="nav-item"><a class="nav-link" href="MemberAccount">Account</a></li>
                    <li class="nav-item"><a class="nav-link" href="logoutauth">Logout</a></li>
                </ul>
            </div>
        </nav>
    </div>
</header>

<!-- Main content -->
<div class="container mt-5">
    <h1 class="text-center mb-4"><i class="fas fa-book-reader me-2"></i>Quản lý mượn sách</h1>

    <!-- Messages -->
    <%
        String message = (String) request.getAttribute("message");
        String errorMessage = (String) request.getAttribute("errorMessage");
        if (message != null) {
    %>
    <div class="alert alert-success"><i class="fas fa-check-circle me-2"></i><%= message %>
    </div>
    <%
        }
        if (errorMessage != null) {
    %>
    <div class="alert alert-danger"><i class="fas fa-exclamation-triangle me-2"></i><%= errorMessage %>
    </div>
    <%
        }
    %>

    <!-- Borrowing Form -->
    <%
        Books selectedBook = (Books) request.getAttribute("selectedBook");
        if (selectedBook != null) {
    %>
    <div class="card">
        <div class="card-header">
            <i class="fas fa-book me-2"></i>Mượn sách: <%= selectedBook.getTitle() %>
        </div>
        <div class="card-body">
            <form action="Borrowing" method="post" id="borrowForm">
                <input type="hidden" name="bookId" value="<%= selectedBook.getIdBook() %>">
                <div class="mb-3">
                    <label for="dueDate" class="form-label"><i class="fas fa-calendar-alt me-2"></i>Ngày đến hạn</label>
                    <input type="date" id="dueDate" name="dueDate" class="form-control" required>
                </div>
                <button type="submit" class="btn btn-primary"><i class="fas fa-plus me-2"></i>Mượn sách</button>
            </form>
        </div>
    </div>
    <%
        }
    %>

    <!-- Search Form -->
    <div class="card mt-4">
        <div class="card-header">
            <i class="fas fa-search me-2"></i>Tìm kiếm sách
        </div>
        <div class="card-body">
            <form action="Borrowing" method="get" class="search-form">
                <input type="text" name="keyword" placeholder="Nhập từ khóa..."
                       value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>"
                       required>
                <button type="submit"><i class="fas fa-search me-2"></i>Tìm kiếm</button>
            </form>
            <%
                List<Books> booksList = (List<Books>) request.getAttribute("booksList");
                if (booksList != null && !booksList.isEmpty()) {
            %>
            <h5>Kết quả tìm kiếm:</h5>
            <div class="list-group">
                <%
                    for (Books book : booksList) {
                %>
                <div class="book-item list-group-item">
                    <span><%= book.getTitle() %> - <%= book.getAuthor() %></span>
                    <a href="Borrowing?id=<%= book.getIdBook() %>" class="btn btn-primary"><i
                            class="fas fa-book me-2"></i>Mượn</a>
                </div>
                <%
                    }
                %>
            </div>
            <%
                }
            %>
        </div>
    </div>

    <!-- Borrowing History -->
    <%
        List<BorrowingHistory> borrowingHistoryList = (List<BorrowingHistory>) request.getAttribute("borrowingHistoryList");
        if (borrowingHistoryList != null && !borrowingHistoryList.isEmpty()) {
    %>
    <div class="card mt-4">
        <div class="card-header">
            <i class="fas fa-history me-2"></i>Lịch sử mượn sách của bạn
        </div>
        <div class="card-body">
            <table class="table table-striped">
                <thead>
                <tr>
                    <th>Tên sách</th>
                    <th>Ngày mượn</th>
                    <th>Ngày trả</th>
                </tr>
                </thead>
                <tbody>
                <%
                    BooksDAO booksDAO = new BooksDAO();
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    for (BorrowingHistory history : borrowingHistoryList) {
                %>
                <tr>
                    <td><%= booksDAO.getBookById(history.getBookId()).getTitle() %>
                    </td>
                    <td><%= dateFormat.format(history.getBorrowDate()) %>
                    </td>
                    <td><%= history.getReturnDate() != null ? dateFormat.format(history.getReturnDate()) : "Chưa trả" %>
                    </td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
        </div>
    </div>
    <%
        }
    %>
</div>

<!-- Footer -->
<footer class="footer_section">
    <p>© 2024 Group 2 All Rights Reserved | Designed by Group 2</p>
</footer>

<!-- JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    document.getElementById('borrowForm')?.addEventListener('submit', function (e) {
        const dueDate = new Date(document.getElementById('dueDate').value);
        const today = new Date();
        today.setHours(0, 0, 0, 0); // Reset time for comparison
        if (dueDate < today) {
            e.preventDefault();
            alert('Ngày đến hạn không thể trước ngày hiện tại!');
        }
    });
</script>
</body>
</html>