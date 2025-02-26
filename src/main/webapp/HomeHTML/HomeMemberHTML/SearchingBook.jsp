<%@ page contentType="text/html;charset=UTF-8" language="java" %>
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
                <a class="navbar-brand" href="index.html">
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
                                <a class="nav-link" href="contact.html">Account</a>
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
                String searchQuery = request.getParameter("searchQuery");
                if (searchQuery != null && !searchQuery.isEmpty()) {
                    // Giả lập dữ liệu - thay bằng logic lấy từ database
                    boolean hasResults = true; // Điều kiện kiểm tra kết quả
                    if (hasResults) {
            %>
            <table class="result-table">
                <thead>
                <tr>
                    <th>Tiêu đề</th>
                    <th>Tác giả</th>
                    <th>Thể loại</th>
                </tr>
                </thead>
                <tbody>
                <tr>
                    <td>Sách Ví Dụ 1</td>
                    <td>Nguyễn Văn A</td>
                    <td>Tiểu thuyết</td>
                </tr>
                <tr>
                    <td>Sách Ví Dụ 2</td>
                    <td>Trần Thị B</td>
                    <td>Khoa học</td>
                </tr>
                <tr>
                    <td>Sách Ví Dụ 3</td>
                    <td>Lê Văn C</td>
                    <td>Lịch sử</td>
                </tr>
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
</div>

<!-- Footer Section -->
<section class="container-fluid footer_section">
    <p>© 2024 Group 2 All Rights Reserved | Design by Group 2</p>
</section>
<!-- End Footer Section -->

<!-- Scripts -->
<script src="HomeHTML/HomeMemberHTML/js/jquery-3.4.1.min.js" type="text/javascript"></script>
<script src="HomeHTML/HomeMemberHTML/js/bootstrap.js" type="text/javascript"></script>
</body>
</html>