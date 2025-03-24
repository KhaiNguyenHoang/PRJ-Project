<%@ page import="java.util.ArrayList" %>
<%@ page import="model.Books" %>
<%@ page import="dao.BookCategoriesDAO" %>
<%@ page import="model.Members" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Members currentMember = (Members) request.getSession().getAttribute("user");
    if (currentMember == null) {
        response.sendRedirect("/Auth/SignIn-SignUp.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>Tìm Kiếm Sách</title>
    <!-- Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,700&display=swap" rel="stylesheet"/>
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600&display=swap" rel="stylesheet"/>
    <!-- Bootstrap CSS -->
    <link href="HomeHTML/HomeMemberHTML/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="HomeHTML/HomeMemberHTML/css/style.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/responsive.css" rel="stylesheet"/>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    <!-- Swiper CSS -->
    <link rel="stylesheet" href="https://unpkg.com/swiper/swiper-bundle.min.css"/>

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background-color: #f0f2f5;
        }

        .hero_area, .footer_section {
            font-family: 'Roboto', sans-serif; /* Giữ font cũ cho header và footer */
        }

        .search-section {
            padding: 60px 0;
            background-color: #ffffff;
            text-align: center;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
        }

        .search-section form {
            display: inline-flex;
            align-items: center;
            margin-bottom: 40px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            border-radius: 8px;
            overflow: hidden;
        }

        .search-section input {
            width: 450px;
            padding: 14px;
            font-size: 1.1rem;
            border: none;
            outline: none;
            background-color: #f9f9f9;
            transition: background-color 0.3s ease;
        }

        .search-section input:focus {
            background-color: #ffffff;
        }

        .search-section button {
            padding: 14px 28px;
            background-color: #3498db;
            color: white;
            border: none;
            font-size: 1.1rem;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
        }

        .search-section button:hover {
            background-color: #2980b9;
        }

        .search-section button:active {
            transform: scale(0.95);
        }

        /* Swiper container */
        .swiper-container {
            width: 100%;
            padding: 20px 0;
            position: relative;
        }

        .swiper-slide {
            display: flex;
            justify-content: center;
        }

        .book-card {
            background-color: #ffffff;
            border-radius: 12px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            width: 220px;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
            text-align: center;
            padding: 15px;
        }

        .book-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 14px rgba(0, 0, 0, 0.15);
        }

        .book-cover {
            width: 100%;
            height: 280px;
            object-fit: cover;
            border-radius: 10px;
            transition: transform 0.3s ease;
        }

        .book-card:hover .book-cover {
            transform: scale(1.05);
        }

        .book-title {
            font-size: 1.2rem;
            font-weight: 600;
            margin: 10px 0;
            color: #2c3e50;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .book-author {
            font-size: 0.95rem;
            color: #7f8c8d;
            margin-bottom: 12px;
            white-space: nowrap;
            overflow: hidden;
            text-overflow: ellipsis;
        }

        .btn-detail {
            display: inline-block;
            padding: 8px 18px;
            background: linear-gradient(135deg, #2ecc71, #27ae60); /* Gradient màu đẹp hơn */
            color: white;
            text-decoration: none;
            border-radius: 20px; /* Bo tròn mạnh hơn */
            font-size: 0.9rem;
            font-weight: 600;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .btn-detail:hover {
            transform: scale(1.1);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        }

        /* Swiper navigation buttons */
        .swiper-button-next, .swiper-button-prev {
            color: #3498db;
            width: 40px;
            height: 40px;
            background-color: rgba(255, 255, 255, 0.9);
            border-radius: 50%;
            transition: background-color 0.3s ease, transform 0.3s ease;
        }

        .swiper-button-next:hover, .swiper-button-prev:hover {
            background-color: #ffffff;
            transform: scale(1.15);
        }

        .swiper-button-next:after, .swiper-button-prev:after {
            font-size: 18px;
            font-weight: bold;
        }

        .no-results {
            text-align: center;
            color: #e74c3c;
            font-size: 1.2rem;
            font-weight: 500;
            margin-top: 40px;
            animation: fadeIn 0.5s ease;
        }

        .no-results i {
            font-size: 2rem;
            margin-bottom: 10px;
            color: #e74c3c;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
            }
            to {
                opacity: 1;
            }
        }

        .spinner {
            display: none;
            margin: 20px auto;
            border: 6px solid #f3f3f3;
            border-top: 6px solid #3498db;
            border-radius: 50%;
            width: 40px;
            height: 40px;
            animation: spin 1s linear infinite;
        }

        @keyframes spin {
            0% {
                transform: rotate(0deg);
            }
            100% {
                transform: rotate(360deg);
            }
        }

        @media (max-width: 768px) {
            .search-section input {
                width: 300px;
            }

            .book-card {
                width: 200px;
            }

            .book-cover {
                height: 260px;
            }
        }

        @media (max-width: 576px) {
            .search-section input {
                width: 100%;
            }

            .search-section button {
                width: 100%;
                border-radius: 8px;
                margin-top: 10px;
            }

            .book-card {
                width: 160px;
            }

            .book-cover {
                height: 220px;
            }
        }
    </style>
</head>
<body>
<div class="hero_area">
    <!-- Header Section -->
    <header class="header_section">
        <div class="container-fluid">
            <nav class="navbar navbar-expand-lg custom_nav-container ">
                <a class="navbar-brand" href="HomePage"><span>Library</span></a>
                <button class="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
                    <span class="navbar-toggler-icon"></span>
                </button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <div class="d-flex ml-auto flex-column flex-lg-row align-items-center">
                        <ul class="navbar-nav">
                            <li class="nav-item active"><a class="nav-link" href="HomePage">Home <span class="sr-only">(current)</span></a>
                            </li>
                            <li class="nav-item"><a class="nav-link" href="HomePage#about_section"> About </a></li>
                            <li class="nav-item"><a class="nav-link" href="HomePage#how_section layout_padding">
                                How </a></li>
                            <li class="nav-item"><a class="nav-link" href="Borrowing"> Borrowing </a></li>
                            <li class="nav-item"><a class="nav-link" href="MemberAccount"> Account </a></li>
                            <li class="nav-item"><a class="nav-link" href="logoutAuth"> Logout</a></li>
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
    <!-- End Header Section -->

    <!-- Search Section -->
    <section class="search-section">
        <form action="SearchingBook" method="post" onsubmit="showSpinner()">
            <input type="text" name="searchQuery" placeholder="Tìm kiếm sách hoặc tác giả..." required/>
            <button type="submit"><i class="fas fa-search"></i> Tìm Kiếm</button>
        </form>

        <!-- Spinner -->
        <div class="spinner" id="spinner"></div>

        <!-- Search Results -->
        <div class="container" id="results-container">
            <%
                ArrayList<Books> books = (ArrayList<Books>) request.getAttribute("books");
                String searchQuery = (String) request.getAttribute("searchQuery");
                BookCategoriesDAO bookCategoriesDAO = new BookCategoriesDAO();
                if (books != null) {
                    if (!books.isEmpty()) {
            %>
            <div class="swiper-container">
                <div class="swiper-wrapper">
                    <%
                        for (Books book : books) {
                    %>
                    <div class="swiper-slide">
                        <div class="book-card">
                            <img class="book-cover" src="<%= book.getFilePath() %>" alt="<%= book.getTitle() %>">
                            <div class="book-title"><%= book.getTitle() %>
                            </div>
                            <div class="book-author"><%= book.getAuthor() %>
                            </div>
                            <a href="BookDetails?id=<%= book.getIdBook() %>" class="btn-detail">Details</a>
                        </div>
                    </div>
                    <%
                        }
                    %>
                </div>
                <!-- Nút điều hướng -->
                <div class="swiper-button-next"></div>
                <div class="swiper-button-prev"></div>
            </div>
            <%
            } else {
            %>
            <div class="no-results">
                <i class="fas fa-exclamation-circle"></i>
                <p>Không tìm thấy kết quả cho: <%= searchQuery %>
                </p>
            </div>
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

<!-- Scripts -->
<script src="HomeHTML/HomeMemberHTML/js/jquery-3.4.1.min.js" type="text/javascript"></script>
<script src="HomeHTML/HomeMemberHTML/js/bootstrap.js" type="text/javascript"></script>
<script src="https://unpkg.com/swiper/swiper-bundle.min.js"></script>
<script>
    function showSpinner() {
        document.getElementById('spinner').style.display = 'block';
        document.getElementById('results-container').style.display = 'none';
    }

    // Khởi tạo Swiper với cuộn chuột và kéo thả
    var swiper = new Swiper('.swiper-container', {
        slidesPerView: 4, // Hiển thị 4 sách cùng lúc
        spaceBetween: 15, // Giảm khoảng cách giữa các card
        loop: true,
        mousewheel: true, // Bật cuộn chuột
        navigation: {
            nextEl: '.swiper-button-next',
            prevEl: '.swiper-button-prev',
        },
    });
</script>
</body>
</html>