<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page session="true" %>
<%@ page import="model.Books, model.BorrowingHistory, model.Borrowing, java.util.List, java.text.SimpleDateFormat" %>
<%@ page import="dao.BorrowingDAO" %>
<%@ page import="dao.BooksDAO" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Book Borrowing Management</title>
    <!-- Owl Carousel -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/OwlCarousel2/2.3.4/assets/owl.carousel.min.css"
          rel="stylesheet"/>
    <!-- Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet"/>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <!-- Animate.css -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css"/>
    <!-- Original Header/Footer CSS -->
    <link href="HomeHTML/HomeMemberHTML/css/bootstrap.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/style.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/responsive.css" rel="stylesheet"/>
    <!-- Custom CSS -->
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: #f5f7fa;
            min-height: 100vh;
        }

        .main-container {
            padding: 40px 0;
        }

        .title-section {
            font-size: 2.5rem;
            font-weight: 700;
            color: #2c3e50;
            margin-bottom: 40px;
            text-align: center;
            letter-spacing: 0.5px;
        }

        .card-modern {
            border: none;
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.05);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .card-modern:hover {
            transform: translateY(-5px);
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
        }

        .card-header-modern {
            background: #2c3e50;
            color: #fff;
            padding: 15px 20px;
            font-size: 1.5rem;
            font-weight: 600;
            border-bottom: none;
            border-radius: 15px 15px 0 0;
        }

        .alert-modern {
            border-radius: 10px;
            padding: 15px 20px;
            margin-bottom: 20px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
            border: none;
        }

        .alert-success {
            background: #d4edda;
            color: #155724;
        }

        .alert-danger {
            background: #f8d7da;
            color: #721c24;
        }

        .alert-info {
            background: #cce5ff;
            color: #004085;
        }

        .form-control-modern {
            border-radius: 8px;
            border: 1px solid #ced4da;
            padding: 10px;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        .form-control-modern:focus {
            border-color: #007bff;
            box-shadow: 0 0 8px rgba(0, 123, 255, 0.2);
        }

        .btn-modern {
            border-radius: 8px;
            padding: 10px 25px;
            font-weight: 600;
            transition: all 0.3s ease;
            border: none;
        }

        .btn-primary-modern {
            background: #007bff;
            color: #fff;
        }

        .btn-primary-modern:hover {
            background: #0056b3;
            transform: translateY(-2px);
        }

        .btn-success-modern {
            background: #28a745;
            color: #fff;
        }

        .btn-success-modern:hover {
            background: #218838;
            transform: translateY(-2px);
        }

        .btn-secondary-modern {
            background: #6c757d;
            color: #fff;
        }

        .btn-secondary-modern:disabled {
            background: #adb5bd;
        }

        .search-container {
            display: flex;
            gap: 10px;
            align-items: center;
        }

        .search-container input {
            flex-grow: 1;
        }

        .book-item {
            padding: 15px;
            background: #fff;
            border-radius: 10px;
            margin-bottom: 10px;
            transition: all 0.3s ease;
            border: 1px solid #e9ecef;
        }

        .book-item:hover {
            background: #f8f9fa;
            transform: translateX(3px);
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
        }

        .table-modern thead th {
            background: #2c3e50;
            color: #fff;
            font-weight: 600;
            padding: 12px;
        }

        .table-modern tbody tr {
            transition: all 0.3s ease;
        }

        .table-modern tbody tr:hover {
            background: #f8f9fa;
            transform: scale(1.005);
        }

        .table-modern td, .table-modern th {
            vertical-align: middle;
        }
    </style>
</head>
<body>
<!-- Header (Restored Original) -->
<header class="header_section">
    <div class="container-fluid">
        <nav class="navbar navbar-expand-lg custom_nav-container ">
            <a class="navbar-brand" href="HomePage">
                <span>Library</span>
            </a>
            <button aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"
                    class="navbar-toggler" data-target="#navbarSupportedContent" data-toggle="collapse" type="button">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <div class="d-flex ml-auto flex-column flex-lg-row align-items-center">
                    <ul class="navbar-nav">
                        <li class="nav-item active">
                            <a class="nav-link" href="HomePage">Home <span class="sr-only">(current)</span></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="HomePage#about_section"> About </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="HomePage#how_section layout_padding"> How </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="Borrowing"> Borrowing </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="MemberAccount"> Account </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="logoutAuth"> Logout</a>
                        </li>
                    </ul>
                    <div class="user_option">
                        <form action="SearchingBook" class="form-inline my-2 my-lg-0 ml-0 ml-lg-4 mb-3 mb-lg-0"
                              method="get">
                            <button class="btn my-2 my-sm-0 nav_search-btn" type="submit"></button>
                        </form>
                    </div>
                </div>
            </div>
        </nav>
    </div>
</header>

<!-- Main Content -->
<div class="container main-container">
    <h1 class="title-section animate__animated animate__fadeInDown">
        <i class="fas fa-book-reader me-2" style="color: #007bff;"></i>Book Borrowing Management
    </h1>

    <!-- Messages -->
    <% String message = (String) request.getAttribute("message"); %>
    <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
    <% String infoMessage = (String) request.getAttribute("infoMessage"); %>
    <% if (message != null) { %>
    <div class="alert-modern alert-success animate__animated animate__fadeIn">
        <i class="fas fa-check-circle me-2"></i><%= message %>
    </div>
    <% } %>
    <% if (errorMessage != null) { %>
    <div class="alert-modern alert-danger animate__animated animate__fadeIn">
        <i class="fas fa-exclamation-triangle me-2"></i><%= errorMessage %>
    </div>
    <% } %>
    <% if (infoMessage != null) { %>
    <div class="alert-modern alert-info animate__animated animate__fadeIn">
        <i class="fas fa-info-circle me-2"></i><%= infoMessage %>
    </div>
    <% } %>

    <!-- Borrowing Form -->
    <% Books selectedBook = (Books) request.getAttribute("selectedBook"); %>
    <% if (selectedBook != null) { %>
    <div class="card-modern mb-4 animate__animated animate__slideInUp">
        <div class="card-header-modern">
            <i class="fas fa-book me-2"></i>Borrow: <%= selectedBook.getTitle() %>
        </div>
        <div class="card-body p-4">
            <form action="Borrowing" method="post" id="borrowForm" class="needs-validation" novalidate>
                <input type="hidden" name="bookId" value="<%= selectedBook.getIdBook() %>">
                <div class="mb-3">
                    <label for="dueDate" class="form-label fw-bold" style="color: #2c3e50;">
                        <i class="fas fa-calendar-alt me-2"></i>Due Date
                    </label>
                    <input type="date" id="dueDate" name="dueDate" class="form-control-modern" required>
                    <div class="invalid-feedback">Please select a valid due date.</div>
                </div>
                <button type="submit" class="btn-modern btn-primary-modern w-100">
                    <i class="fas fa-plus me-2"></i>Borrow Book
                </button>
            </form>
        </div>
    </div>
    <% } %>

    <!-- Search Form (Hidden when borrowing) -->
    <% if (selectedBook == null) { %>
    <div class="card-modern mb-4 animate__animated animate__slideInUp">
        <div class="card-header-modern">
            <i class="fas fa-search me-2"></i>Search Books
        </div>
        <div class="card-body p-4">
            <form action="Borrowing" method="get" class="search-container mb-3">
                <input type="text" name="keyword" placeholder="Search by title or author..."
                       class="form-control-modern"
                       value="<%= request.getParameter("keyword") != null ? request.getParameter("keyword") : "" %>"
                       required>
                <button type="submit" class="btn-modern btn-primary-modern">
                    <i class="fas fa-search me-2"></i>Search
                </button>
            </form>
            <% List<Books> booksList = (List<Books>) request.getAttribute("booksList"); %>
            <% if (booksList != null && !booksList.isEmpty()) { %>
            <h5 class="fw-bold mb-3" style="color: #2c3e50;">Search Results:</h5>
            <div class="list-group">
                <% for (Books book : booksList) { %>
                <div class="book-item d-flex justify-content-between align-items-center animate__animated animate__fadeIn">
                    <span class="fw-medium"
                          style="color: #2c3e50;"><%= book.getTitle() %> - <%= book.getAuthor() %></span>
                    <a href="Borrowing?id=<%= book.getIdBook() %>" class="btn-modern btn-primary-modern">
                        <i class="fas fa-book me-2"></i>Borrow
                    </a>
                </div>
                <% } %>
            </div>
            <% } %>
        </div>
    </div>
    <% } %>

    <!-- Borrowing History -->
    <% List<BorrowingHistory> borrowingHistoryList = (List<BorrowingHistory>) request.getAttribute("borrowingHistoryList"); %>
    <% if (borrowingHistoryList != null) { %>
    <div class="card-modern animate__animated animate__slideInUp">
        <div class="card-header-modern">
            <i class="fas fa-history me-2"></i>Borrowing History
        </div>
        <div class="card-body p-4">
            <% if (!borrowingHistoryList.isEmpty()) { %>
            <div class="table-responsive">
                <table class="table-modern table align-middle">
                    <thead>
                    <tr>
                        <th>Title</th>
                        <th>Borrow Date</th>
                        <th>Return Date</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <%
                        BorrowingDAO borrowingDAO = new BorrowingDAO();
                        BooksDAO booksDAO = new BooksDAO();
                        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                    %>
                    <% for (BorrowingHistory history : borrowingHistoryList) { %>
                    <% Borrowing borrowing = history.getReturnDate() == null ? borrowingDAO.getBorrowingByMemberIdAndBookCopyId(history.getMemberId(), history.getBookCopyId()) : null; %>
                    <tr>
                        <td><%= history.getBookTitle() != null ? history.getBookTitle() : "Unknown Title" %>
                        </td>
                        <td><%= dateFormat.format(history.getBorrowDate()) %>
                        </td>
                        <td><%= history.getReturnDate() != null ? dateFormat.format(history.getReturnDate()) : "Not Returned" %>
                        </td>
                        <td>
                            <% if (history.getReturnDate() == null && borrowing != null) { %>
                            <form action="Returning" method="post" style="display:inline;">
                                <input type="hidden" name="borrowId" value="<%= borrowing.getIdBorrow() %>">
                                <button type="submit" class="btn-modern btn-success-modern">
                                    <i class="fas fa-undo me-2"></i>Return
                                </button>
                            </form>
                            <% } else { %>
                            <button class="btn-modern btn-secondary-modern" disabled>
                                <i class="fas fa-undo me-2"></i>Returned
                            </button>
                            <% } %>
                        </td>
                    </tr>
                    <% } %>
                    </tbody>
                </table>
            </div>
            <% } else { %>
            <p class="text-center text-muted animate__animated animate__fadeIn">No borrowing history available.</p>
            <% } %>
        </div>
    </div>
    <% } %>
</div>

<!-- Footer (Restored Original) -->
<footer class="footer_section">
    <p>© 2024 Group 2 All Rights Reserved | Designed by Group 2</p>
</footer>

<!-- JavaScript -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Form validation
    document.getElementById('borrowForm')?.addEventListener('submit', function (e) {
        const dueDate = new Date(document.getElementById('dueDate').value);
        const today = new Date();
        today.setHours(0, 0, 0, 0);

        if (dueDate < today) {
            e.preventDefault();
            alert('Due date cannot be earlier than today!');
            return;
        }

        if (!this.checkValidity()) {
            e.preventDefault();
            e.stopPropagation();
        }
        this.classList.add('was-validated');
    });

    // Enhanced hover effects
    document.querySelectorAll('.card-modern').forEach(card => {
        card.addEventListener('mouseenter', () => {
            card.style.transition = 'transform 0.3s ease, box-shadow 0.3s ease';
        });
    });

    document.querySelectorAll('.book-item').forEach(item => {
        item.addEventListener('mouseenter', () => {
            item.querySelector('a').style.transform = 'scale(1.05)';
        });
        item.addEventListener('mouseleave', () => {
            item.querySelector('a').style.transform = 'scale(1)';
        });
    });

    document.querySelectorAll('.table-modern tbody tr').forEach(row => {
        row.addEventListener('mouseenter', () => {
            row.querySelectorAll('button').forEach(btn => {
                if (!btn.disabled) btn.style.transform = 'scale(1.05)';
            });
        });
        row.addEventListener('mouseleave', () => {
            row.querySelectorAll('button').forEach(btn => {
                if (!btn.disabled) btn.style.transform = 'scale(1)';
            });
        });
    });
</script>
</body>
</html>