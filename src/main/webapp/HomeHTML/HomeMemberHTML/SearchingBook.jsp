<%@ page import="java.util.ArrayList" %>
<%@ page import="model.entity.Books" %>
<%@ page import="model.dao.BookCategoriesDAO" %>
<%@ page import="model.entity.Members" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    // Retrieve the current logged-in member from the session
    Members currentMember = (Members) request.getSession().getAttribute("user");

    // If no member is logged in, redirect to the Login page
    if (currentMember == null) {
        response.sendRedirect("/Auth/SignIn-SignUp.jsp");
        return;  // Prevent further page processing after redirect
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>Tìm Kiếm Sách</title>
    <!-- Fonts style -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,700&display=swap" rel="stylesheet"/>
    <!-- Bootstrap CSS -->
    <link href="HomeHTML/HomeMemberHTML/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="HomeHTML/HomeMemberHTML/css/style.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/responsive.css" rel="stylesheet"/>
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>

    <style>
        /* CSS cho phần tìm kiếm và bảng kết quả */
        .search-section {
            padding: 40px 0;
            background-color: #f8f9fa; /* Màu nền nhẹ */
            text-align: center;
        }

        .search-section form {
            display: inline-block;
            margin-bottom: 20px;
        }

        .search-section input {
            width: 400px;
            padding: 10px;
            font-size: 1.1rem;
            border: 1px solid #ddd;
            border-radius: 5px 0 0 5px;
            outline: none;
        }

        .search-section button {
            padding: 10px 20px;
            background-color: #e74c3c;
            color: white;
            border: none;
            border-radius: 0 5px 5px 0;
            font-size: 1.1rem;
            cursor: pointer;
        }

        .search-section button:hover {
            background-color: #c0392b;
        }

        .result-table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }

        .result-table th, .result-table td {
            padding: 15px;
            text-align: left;
            border-bottom: 1px solid #ddd;
        }

        .result-table th {
            background-color: #2c3e50;
            color: white;
        }

        .result-table tr:hover {
            background-color: #f1f1f1;
        }

        .no-results {
            text-align: center;
            color: #e74c3c;
            font-size: 1.2rem;
            font-weight: 500;
        }
    </style>
</head>
<body>
<div class="hero_area">
    <!-- Header Section -->
    <header class="header_section">
        <div class="container-fluid">
            <nav class="navbar navbar-expand-lg custom_nav-container">
                <a class="navbar-brand" href="index.jsp">
                    <span>Library</span>
                </a>
                <button class="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent"
                        aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <div class="d-flex ml-auto flex-column flex-lg-row align-items-center">
                        <ul class="navbar-nav">
                            <li class="nav-item active">
                                <a class="nav-link" href="HomePage">Home <span class="sr-only">(current)</span></a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#about_section">About</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="#how_section">How</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="contact.html">Borrowing</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="MemberAccount">Account</a>
                            </li>
                            <li class="nav-item">
                                <a class="nav-link" href="logoutauth">Logout</a>
                            </li>
                        </ul>
                    </div>
                </div>
            </nav>
        </div>
    </header>
    <!-- End Header Section -->

    <!-- Search Section -->
    <section class="search-section">
        <form action="SearchingBook" method="post">
            <input type="text" name="searchQuery" placeholder="Tìm kiếm sách hoặc tác giả..." required/>
            <button type="submit"><i class="fas fa-search"></i> Tìm Kiếm</button>
        </form>

        <!-- Search Results -->
        <div class="container">
            <%
                ArrayList<Books> books = (ArrayList<Books>) request.getAttribute("books");
                String searchQuery = (String) request.getAttribute("searchQuery");
                BookCategoriesDAO bookCategoriesDAO = new BookCategoriesDAO();
                if (books != null) {
                    if (books.size() > 0) {
            %>
            <table class="result-table">
                <!-- Chỉ định tiêu đề bảng một lần -->
                <thead>
                <tr>
                    <th>Title</th>
                    <th>Author</th>
                    <th>ISBN</th>
                    <th>Category</th>
                    <th>Digital</th>
                    <th>Year Published</th>
                    <th>Publisher</th>
                    <th>Copies Available</th>
                    <th>Details</th>
                </tr>
                </thead>
                <tbody>
                <%
                    // Lặp qua tất cả các sách và hiển thị trong tbody
                    for (Books book : books) {
                %>
                <tr>
                    <td><%= book.getTitle() %>
                    </td>
                    <td><%= book.getAuthor() %>
                    </td>
                    <td><%= book.getIsbn() %>
                    </td>
                    <td><%= bookCategoriesDAO.getBookCategoryById(book.getCategoryId()).getCategoryName() %>
                    </td>
                    <td><%= book.isDigital() ? "Yes" : "No" %>
                    </td>
                    <td><%= book.getYearPublished() %>
                    </td>
                    <td><%= book.getPublisher() %>
                    </td>
                    <td><%= book.getCopiesAvailable() %>
                    </td>
                    <td>
                        <!-- Thay đổi href để gửi request tới Servlet -->
                        <a href="BookDetails?id=<%= book.getIdBook() %>" class="btn">Details</a>
                    </td>
                </tr>
                <%
                    }
                %>
                </tbody>
            </table>
            <%
            } else {
            %>
            <p class="no-results">Không tìm thấy kết quả cho: <%= searchQuery %>
            </p>
            <%
                    }
                }
            %>
        </div>
    </section>

</div>    <!-- Footer Section -->
<section class="container-fluid footer_section">
    <p>© 2024 Group 2 All Rights Reserved | Design by Group 2</p>
</section>
<!-- End Footer Section -->

<!-- Scripts -->
<script src="HomeHTML/HomeMemberHTML/js/jquery-3.4.1.min.js" type="text/javascript"></script>
<script src="HomeHTML/HomeMemberHTML/js/bootstrap.js" type="text/javascript"></script>
</body>
</html>
