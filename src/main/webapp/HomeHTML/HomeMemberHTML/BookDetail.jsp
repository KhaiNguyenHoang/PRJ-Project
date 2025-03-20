<%@ page import="model.Books" %>
<%@ page import="model.BookDetail" %>
<%@ page import="dao.BooksDAO" %>
<%@ page import="dao.BookDetailDAO" %>
<%@ page import="model.Members" %>
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

        /* PDF Viewer Styles */
        .pdf-viewer {
            margin-top: 30px;
            border: 1px solid #ddd;
            border-radius: 8px;
            padding: 20px;
            background-color: #fff;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
            transition: all 0.3s ease;
        }

        .pdf-controls {
            display: flex;
            justify-content: center;
            gap: 15px;
            margin-bottom: 20px;
        }

        .pdf-controls button {
            background-color: #3498db;
            color: white;
            border: none;
            padding: 10px 20px;
            cursor: pointer;
            border-radius: 6px;
            font-size: 1rem;
            transition: background-color 0.3s ease;
        }

        .pdf-controls button:hover {
            background-color: #2980b9;
        }

        #pdfContainer canvas {
            display: block;
            margin: 0 auto;
            border-radius: 4px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            max-width: 100%;
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

            .pdf-controls {
                flex-direction: column;
                gap: 10px;
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
                <a class="navbar-brand" href="index.jsp">
                    <span>Library</span>
                </a>
                <button class="navbar-toggler" type="button" data-toggle="collapse"
                        data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent"
                        aria-expanded="false" aria-label="Toggle navigation">
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
                                <a class="nav-link" href="Borrowing">Borrowing</a>
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

    <!-- Book Details Section -->
    <section class="book-details-section">
        <div class="container">
            <%
                String bookIdParam = request.getParameter("id");
                Books book = null;
                BookDetail bookDetail = null;
                if (bookIdParam != null) {
                    try {
                        int bookId = Integer.parseInt(bookIdParam);
                        BooksDAO booksDAO = new BooksDAO();
                        book = booksDAO.getBookById(bookId);
                        BookDetailDAO bookDetailDAO = new BookDetailDAO();
                        bookDetail = bookDetailDAO.getBookDetailByBookID(bookId);

                        if (book != null && bookDetail != null) {
            %>
            <div class="book-details">
                <div class="book-info">
                    <!-- Book Cover Image -->
                    <img src="<%=book.getFilePath()%>" alt="Cover of <%=book.getTitle()%>" class="book-cover"/>
                    <!-- Book Information -->
                    <div>
                        <h2><%=book.getTitle()%>
                        </h2>
                        <p><strong>Author:</strong> <%=book.getAuthor()%>
                        </p>
                        <p><strong>ISBN:</strong> <%=book.getIsbn()%>
                        </p>
                        <p><strong>Publisher:</strong> <%=book.getPublisher()%>
                        </p>
                        <p><strong>Year Published:</strong> <%=book.getYearPublished()%>
                        </p>
                        <p><strong>Copies Available:</strong> <%=book.getCopiesAvailable()%>
                        </p>
                    </div>
                </div>

                <!-- Book Description -->
                <div class="book-description">
                    <h3>Book Description</h3>
                    <p><%=bookDetail.getDescription()%>
                    </p>
                </div>

                <!-- PDF Viewer (if digital) -->
                <% if (book.isDigital() && bookDetail.getPdfPath() != null && !bookDetail.getPdfPath().isEmpty()) { %>
                <div class="pdf-viewer">
                    <div class="pdf-controls">
                        <button onclick="prevPage()">Previous Page</button>
                        <button onclick="nextPage()">Next Page</button>
                        <button onclick="zoomIn()">Zoom In</button>
                        <button onclick="zoomOut()">Zoom Out</button>
                    </div>
                    <div id="pdfContainer"></div>
                </div>
                <% } else { %>
                <p>This book is not available in digital format or no PDF is provided.</p>
                <% } %>

                <!-- Borrow Book Button -->
                <form action="BorrowBookServlet" method="post">
                    <input type="hidden" name="bookId" value="<%=book.getIdBook()%>">
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
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.4.456/pdf.min.js"></script>
<script>
    let pdfDoc = null;
    let pageNum = 1;
    let scale = 1.5;
    const pdfContainer = document.getElementById('pdfContainer');

    // Load PDF
    <% if (book != null && book.isDigital() && bookDetail.getPdfPath() != null && !bookDetail.getPdfPath().isEmpty()) {
                %>
    pdfjsLib.getDocument('<%=bookDetail.getPdfPath()%>').promise.then(function (pdf) {
        pdfDoc = pdf;
        renderPage(pageNum);
    }).catch(function (error) {
        console.error('Error loading PDF: ', error);
        pdfContainer.innerHTML = '<p>Error loading PDF preview.</p>';
    });
    <% } %>

    // Render PDF page
    function renderPage(num) {
        pdfContainer.innerHTML = ''; // Clear previous canvas
        pdfDoc.getPage(num).then(function (page) {
            const viewport = page.getViewport({scale: scale});
            const canvas = document.createElement('canvas');
            canvas.height = viewport.height;
            canvas.width = viewport.width;
            pdfContainer.appendChild(canvas);
            const context = canvas.getContext('2d');
            page.render({canvasContext: context, viewport: viewport});
        });
    }

    // Previous page
    function prevPage() {
        if (pageNum <= 1) return;
        pageNum--;
        renderPage(pageNum);
    }

    // Next page
    function nextPage() {
        if (pageNum >= pdfDoc.numPages) return;
        pageNum++;
        renderPage(pageNum);
    }

    // Zoom in
    function zoomIn() {
        scale += 0.5;
        renderPage(pageNum);
    }

    // Zoom out
    function zoomOut() {
        if (scale <= 0.5) return;
        scale -= 0.5;
        renderPage(pageNum);
    }
</script>
</body>
</html>