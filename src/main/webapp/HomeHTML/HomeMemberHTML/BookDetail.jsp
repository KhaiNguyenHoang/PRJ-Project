<%@ page import="model.entity.Books" %>
<%@ page import="model.entity.BookDetail" %>
<%@ page import="model.dao.BooksDAO" %>
<%@ page import="model.dao.BookDetailDAO" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>Book Details</title>

    <!-- Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&family=Open+Sans:wght@400;600&display=swap"
          rel="stylesheet"/>
    <!-- Bootstrap CSS -->
    <link href="HomeHTML/HomeMemberHTML/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="HomeHTML/HomeMemberHTML/css/style.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/responsive.css" rel="stylesheet"/>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>

    <style>
        body {
            font-family: 'Open Sans', sans-serif;
            background-color: #f7f7f7;
            color: #333;
            margin: 0;
            padding: 0;
        }

        .hero_area {
            background-color: #fff;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .book-details-section {
            padding: 60px 0;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
            padding: 0 15px;
        }

        .book-details {
            background-color: #fff;
            padding: 30px;
            border-radius: 10px;
            box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
        }

        .book-info {
            display: flex;
            gap: 40px;
            margin-bottom: 30px;
        }

        .book-cover {
            width: 220px;
            height: 330px;
            object-fit: cover;
            border-radius: 12px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .book-cover:hover {
            transform: scale(1.05);
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
        }

        .book-info h2 {
            font-family: 'Roboto', sans-serif;
            font-size: 2.2rem;
            color: #2c3e50;
            margin-bottom: 20px;
        }

        .book-info p {
            font-size: 1.1rem;
            margin-bottom: 15px;
            line-height: 1.6;
        }

        .book-description {
            margin-top: 30px;
        }

        .book-description h3 {
            font-family: 'Roboto', sans-serif;
            font-size: 1.6rem;
            color: #2c3e50;
            margin-bottom: 15px;
        }

        .book-description p {
            font-size: 1.1rem;
            line-height: 1.8;
            color: #555;
        }

        .pdf-container {
            max-height: 0;
            opacity: 0;
            overflow: hidden;
            transition: max-height 0.5s ease, opacity 0.5s ease;
            margin-top: 20px;
        }

        .pdf-container.open {
            max-height: 700px;
            opacity: 1;
        }

        .preview-button {
            background-color: #3498db;
            color: white;
            border: none;
            padding: 12px 25px;
            cursor: pointer;
            border-radius: 6px;
            font-size: 1.1rem;
            transition: background-color 0.3s ease;
            margin-top: 20px;
        }

        .preview-button:hover {
            background-color: #2980b9;
        }

        .borrow-button {
            background-color: #2ecc71;
            color: white;
            border: none;
            padding: 12px 25px;
            cursor: pointer;
            border-radius: 6px;
            font-size: 1.1rem;
            transition: background-color 0.3s ease;
            margin-top: 20px;
        }

        .borrow-button:hover {
            background-color: #27ae60;
        }

        .back-btn {
            text-decoration: none;
            font-size: 1.2rem;
            margin-top: 30px;
            display: inline-block;
            padding: 12px 25px;
            border-radius: 6px;
            background-color: #ecf0f1;
            color: #333;
            transition: background-color 0.3s ease;
        }

        .back-btn:hover {
            background-color: #bdc3c7;
        }

        @media (max-width: 768px) {
            .book-info {
                flex-direction: column;
                align-items: center;
            }

            .book-cover {
                margin-bottom: 30px;
            }
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

    <!-- Book Details Section -->
    <section class="book-details-section">
        <div class="container">
            <%
                String bookIdParam = request.getParameter("id");
                if (bookIdParam != null) {
                    try {
                        int bookId = Integer.parseInt(bookIdParam);
                        BooksDAO booksDAO = new BooksDAO();
                        Books book = booksDAO.getBookById(bookId);
                        BookDetailDAO bookDetailDAO = new BookDetailDAO();
                        BookDetail bookDetail = bookDetailDAO.getBookDetailByBookID(bookId);

                        if (book != null && bookDetail != null) {
            %>
            <div class="book-details">
                <div class="book-info">
                    <!-- Book Cover Image -->
                    <img src="<%= book.getFilePath() %>" alt="Cover of <%= book.getTitle() %>" class="book-cover"/>
                    <!-- Book Information -->
                    <div>
                        <h2><%= book.getTitle() %>
                        </h2>
                        <p><strong>Author:</strong> <%= book.getAuthor() %>
                        </p>
                        <p><strong>ISBN:</strong> <%= book.getIsbn() %>
                        </p>
                        <p><strong>Publisher:</strong> <%= book.getPublisher() %>
                        </p>
                        <p><strong>Year Published:</strong> <%= book.getYearPublished() %>
                        </p>
                        <p><strong>Copies Available:</strong> <%= book.getCopiesAvailable() %>
                        </p>
                    </div>
                </div>

                <!-- Book Description -->
                <div class="book-description">
                    <h3>Book Description</h3>
                    <p><%= bookDetail.getDescription() %>
                    </p>
                </div>

                <!-- PDF Preview (if digital) -->
                <% if (book.isDigital()) { %>
                <% if (bookDetail.getPdfPath() != null && !bookDetail.getPdfPath().isEmpty()) { %>
                <button id="previewBtn" class="preview-button">Read Preview</button>
                <div id="pdfContainer" class="pdf-container">
                    <iframe src="<%= bookDetail.getPdfPath() %>" width="100%" height="600px"
                            title="PDF Preview of <%= book.getTitle() %>"></iframe>
                </div>
                <% } else { %>
                <p>No PDF available for this book.</p>
                <% } %>
                <% } else { %>
                <p>This book is not available in digital format.</p>
                <% } %>

                <!-- Borrow Book Button -->
                <form action="BorrowBookServlet" method="post">
                    <input type="hidden" name="bookId" value="<%= book.getIdBook() %>">
                    <button type="submit" class="borrow-button">Borrow Book</button>
                </form>

                <!-- Back to Search Button -->
                <a href="SearchingBook" class="back-btn">Back to Search</a>
            </div>
            <%
            } else {
            %>
            <p>Book details not found.</p>
            <%
                }
            } catch (NumberFormatException e) {
            %>
            <p>Invalid book ID.</p>
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
<script>
    const previewBtn = document.getElementById('previewBtn');
    const pdfContainer = document.getElementById('pdfContainer');
    if (previewBtn && pdfContainer) {
        previewBtn.addEventListener('click', function () {
            pdfContainer.classList.toggle('open');
            if (pdfContainer.classList.contains('open')) {
                previewBtn.textContent = "Close Preview";
            } else {
                previewBtn.textContent = "Read Preview";
            }
        });
    }
</script>
</body>
</html>