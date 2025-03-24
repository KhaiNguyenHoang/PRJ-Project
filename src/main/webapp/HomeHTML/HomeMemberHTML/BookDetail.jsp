<%@ page import="model.Books" %>
<%@ page import="model.BookDetail" %>
<%@ page import="dao.BooksDAO" %>
<%@ page import="dao.BookDetailDAO" %>
<%@ page import="model.Members" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Members currentMember = (Members) request.getSession().getAttribute("user");
    if (currentMember == null) {
        response.sendRedirect("HomePage");
        return;
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
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet"/>
    <!-- Bootstrap CSS -->
    <link href="HomeHTML/HomeMemberHTML/css/bootstrap.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/style.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/responsive.css" rel="stylesheet"/>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>

    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: linear-gradient(135deg, #f0f2f5, #e0e4e8);
            color: #333;
            margin: 0;
            padding: 0;
            overflow-x: hidden;
        }

        .hero_area {
            background: #fff;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            position: sticky;
            top: 0;
            z-index: 1000;
        }

        .book-details-section {
            padding: 80px 0;
            min-height: 100vh;
        }

        .container {
            max-width: 1100px;
            margin: 0 auto;
            padding: 0 20px;
        }

        .book-details {
            background: #fff;
            padding: 50px;
            border-radius: 20px;
            box-shadow: 0 10px 30px rgba(0, 0, 0, 0.15);
            animation: fadeIn 0.5s ease-in-out;
        }

        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .book-info {
            display: flex;
            align-items: center;
            gap: 40px;
            padding-bottom: 40px;
            border-bottom: 2px solid #eee;
        }

        .book-cover-wrapper {
            position: relative;
            width: 300px;
            flex-shrink: 0;
        }

        .book-cover {
            width: 100%;
            height: 450px;
            object-fit: cover;
            border-radius: 15px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            transition: transform 0.4s ease, box-shadow 0.4s ease;
        }

        .book-cover:hover {
            transform: scale(1.05);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.3);
        }

        .book-info-content h2 {
            font-size: 3rem;
            font-weight: 700;
            color: #1a2a44;
            margin-bottom: 25px;
            line-height: 1.2;
        }

        .book-info-content p {
            font-size: 1.2rem;
            margin-bottom: 15px;
            line-height: 1.8;
            color: #666;
        }

        .book-description {
            margin: 40px 0;
        }

        .book-description h3 {
            font-size: 2rem;
            font-weight: 600;
            color: #1a2a44;
            margin-bottom: 20px;
        }

        .book-description p {
            font-size: 1.2rem;
            line-height: 2;
            color: #555;
        }

        /* PDF Viewer Styles */
        .pdf-viewer {
            position: relative;
            background: #fff;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.1);
            margin-top: 40px;
            transition: all 0.3s ease;
            overflow: hidden;
        }

        .pdf-container {
            border-radius: 10px;
            background: #fff;
            max-height: 700px;
            overflow: hidden;
            display: flex;
            justify-content: center;
            align-items: center;
            transition: transform 0.3s ease;
        }

        .pdf-container canvas {
            max-width: 100%;
            max-height: 100%;
            border-radius: 8px;
            transition: opacity 0.3s ease;
        }

        .pdf-header {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-bottom: 15px;
        }

        .pdf-header h4 {
            font-size: 1.8rem;
            font-weight: 600;
            color: #1a2a44;
            margin: 0;
        }

        .pdf-controls {
            display: flex;
            gap: 10px;
            flex-wrap: wrap;
            align-items: center;
        }

        .pdf-controls button {
            background: linear-gradient(135deg, #3498db, #2980b9);
            color: white;
            border: none;
            padding: 10px 20px;
            border-radius: 20px;
            font-size: 1rem;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.3s ease, box-shadow 0.3s ease, opacity 0.3s ease;
        }

        .pdf-controls button:hover {
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
            opacity: 0.9;
        }

        .pdf-controls .pdf-fullscreen {
            background: linear-gradient(135deg, #e74c3c, #c0392b);
        }

        .page-info {
            font-size: 1rem;
            font-weight: 500;
            color: #1a2a44;
            margin: 0 10px;
            align-self: center;
        }

        .pdf-controls input[type="number"] {
            background: #f0f2f5;
            border: 1px solid #ddd;
            color: #333;
            font-size: 1rem;
            text-align: center;
            width: 60px;
            padding: 5px;
            border-radius: 5px;
        }

        .pdf-controls input[type="number"]:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.5);
        }

        .fullscreen-nav {
            display: none;
            position: fixed;
            top: 50%;
            width: 100%;
            left: 0;
            transform: translateY(-50%);
            z-index: 1001;
            pointer-events: none;
        }

        .fullscreen-btn {
            pointer-events: all;
            position: absolute;
            background: rgba(0, 0, 0, 0.6);
            color: white;
            border: none;
            padding: 12px;
            border-radius: 50%;
            font-size: 1.2rem;
            cursor: pointer;
            transition: background 0.3s ease, transform 0.3s ease, opacity 0.3s ease;
        }

        .fullscreen-btn:hover {
            background: rgba(0, 0, 0, 0.8);
            transform: scale(1.1);
            opacity: 1;
        }

        .prev-btn {
            left: 30px;
        }

        .next-btn {
            right: 30px;
        }

        .pdf-viewer:fullscreen .fullscreen-nav {
            display: block;
        }

        .pdf-viewer:fullscreen .pdf-container {
            max-height: 100vh;
            width: 100vw;
            height: 100vh;
            padding: 0;
            margin: 0;
            background: #000;
        }

        .pdf-viewer:fullscreen .pdf-container canvas {
            max-width: none;
            max-height: none;
            width: 100vw;
            height: 100vh;
            object-fit: contain;
        }

        .pdf-viewer:fullscreen .pdf-header {
            position: fixed;
            top: 10px;
            left: 50%;
            transform: translateX(-50%);
            z-index: 1001;
            background: rgba(255, 255, 255, 0.9);
            padding: 5px 15px;
            border-radius: 10px;
            box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        }

        .action-buttons {
            display: flex;
            gap: 20px;
            margin-top: 40px;
            flex-wrap: wrap;
            justify-content: center;
        }

        .borrow-button, .back-btn, .favorite-btn {
            padding: 14px 35px;
            border-radius: 30px;
            font-size: 1.2rem;
            font-weight: 600;
            text-align: center;
            text-decoration: none;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .borrow-button {
            background: linear-gradient(135deg, #2ecc71, #27ae60);
            color: white;
        }

        .back-btn {
            background: linear-gradient(135deg, #ecf0f1, #bdc3c7);
            color: #333;
        }

        .favorite-btn {
            background: linear-gradient(135deg, #f1c40f, #e67e22);
            color: white;
        }

        .borrow-button:hover, .back-btn:hover, .favorite-btn:hover {
            transform: translateY(-5px);
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.2);
        }

        .no-pdf, .error-message {
            text-align: center;
            padding: 20px;
            font-size: 1.2rem;
            color: #e74c3c;
        }

        @media (max-width: 768px) {
            .book-info {
                flex-direction: column;
                align-items: center;
                text-align: center;
            }

            .book-cover-wrapper {
                width: 250px;
            }

            .book-cover {
                height: 375px;
            }

            .book-info-content h2 {
                font-size: 2.2rem;
            }

            .book-description h3 {
                font-size: 1.6rem;
            }

            .pdf-controls {
                gap: 8px;
            }

            .action-buttons {
                flex-direction: column;
                gap: 15px;
            }
        }

        @media (max-width: 576px) {
            .book-details {
                padding: 30px;
            }

            .book-cover-wrapper {
                width: 200px;
            }

            .book-cover {
                height: 300px;
            }

            .book-info-content h2 {
                font-size: 1.8rem;
            }
        }
    </style>
</head>
<body>
<div class="hero_area">
    <header class="header_section">
        <div class="container-fluid">
            <nav class="navbar navbar-expand-lg custom_nav-container">
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
                            <li class="nav-item"><a class="nav-link" href="HomePage#about_section">About</a></li>
                            <li class="nav-item"><a class="nav-link" href="HomePage#how_section">How</a></li>
                            <li class="nav-item"><a class="nav-link" href="Borrowing">Borrowing</a></li>
                            <li class="nav-item"><a class="nav-link" href="MemberAccount">Account</a></li>
                            <li class="nav-item"><a class="nav-link" href="logoutAuth">Logout</a></li>
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
</div>

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
                <div class="book-cover-wrapper">
                    <img src="<%= book.getFilePath() %>" alt="Cover of <%= book.getTitle() %>" class="book-cover"/>
                </div>
                <div class="book-info-content">
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

            <div class="book-description">
                <h3>Book Description</h3>
                <p><%= bookDetail.getDescription() %>
                </p>
            </div>

            <% if (book.isDigital() && bookDetail.getPdfPath() != null && !bookDetail.getPdfPath().isEmpty()) { %>
            <div class="pdf-viewer">
                <div class="pdf-header">
                    <h4>PDF Preview</h4>
                    <div class="pdf-controls">
                        <button onclick="prevPage()" title="Previous Page"><i class="fas fa-arrow-left"></i></button>
                        <span id="pageInfo" class="page-info"></span>
                        <input type="number" id="goToPage" min="1" placeholder="Go to" onchange="goToPage(this.value)">
                        <button onclick="nextPage()" title="Next Page"><i class="fas fa-arrow-right"></i></button>
                        <button onclick="zoomIn()" title="Zoom In"><i class="fas fa-search-plus"></i></button>
                        <button onclick="zoomOut()" title="Zoom Out"><i class="fas fa-search-minus"></i></button>
                        <button onclick="toggleFullScreen()" class="pdf-fullscreen" title="Fullscreen"><i
                                class="fas fa-expand"></i></button>
                    </div>
                </div>
                <div id="pdfContainer" class="pdf-container">
                    <canvas id="pdfCanvas"></canvas>
                </div>
                <div class="fullscreen-nav">
                    <button class="fullscreen-btn prev-btn" onclick="prevPage()"><i class="fas fa-arrow-left"></i>
                    </button>
                    <button class="fullscreen-btn next-btn" onclick="nextPage()"><i class="fas fa-arrow-right"></i>
                    </button>
                </div>
            </div>
            <% } else { %>
            <div class="no-pdf">
                <p><i class="fas fa-exclamation-circle"></i> This book is not available in digital format or no PDF is
                    provided.</p>
            </div>
            <% } %>

            <div class="action-buttons">
                <a href="Borrowing?id=<%= book.getIdBook() %>" class="borrow-button"><i class="fas fa-book"></i> Borrow
                    Book</a>
                <a href="#" class="favorite-btn"><i class="fas fa-heart"></i> Add to Favorites</a>
                <a href="SearchingBook" class="back-btn"><i class="fas fa-arrow-left"></i> Back to Search</a>
            </div>
        </div>
        <% } else { %>
        <p class="error-message"><i class="fas fa-exclamation-triangle"></i> Book details not found.</p>
        <% }
        } catch (NumberFormatException e) { %>
        <p class="error-message"><i class="fas fa-exclamation-triangle"></i> Invalid book ID.</p>
        <% }
        } %>
    </div>
</section>

<section class="container-fluid footer_section">
    <p>© 2024 Group 2 All Rights Reserved | Design by Group 2</p>
</section>

<script src="HomeHTML/HomeMemberHTML/js/jquery-3.4.1.min.js"></script>
<script src="HomeHTML/HomeMemberHTML/js/bootstrap.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.11.174/pdf.min.js"></script>
<script>
    let pdfDoc = null;
    let pageNum = 1;
    let pageCount = 0;
    let scale = 1.5;
    let rendering = false;
    const pdfContainer = document.getElementById('pdfContainer');
    const pdfCanvas = document.getElementById('pdfCanvas');
    const pageInfo = document.getElementById('pageInfo');
    const context = pdfCanvas ? pdfCanvas.getContext('2d') : null;

    <% if (book != null && book.isDigital() && bookDetail.getPdfPath() != null && !bookDetail.getPdfPath().isEmpty()) { %>
    pdfjsLib.getDocument('<%= bookDetail.getPdfPath() %>').promise.then(function (pdf) {
        pdfDoc = pdf;
        pageCount = pdf.numPages;
        document.getElementById('goToPage').max = pageCount;
        updatePageInfo();
        renderPage(pageNum);
    }).catch(function (error) {
        console.error('Error loading PDF: ', error);
        pdfContainer.innerHTML = '<p class="error-message"><i class="fas fa-exclamation-circle"></i> Error loading PDF preview.</p>';
    });
    <% } %>

    function updatePageInfo() {
        pageInfo.textContent = `Page ${pageNum} of ${pageCount}`;
    }

    function renderPage(num) {
        if (rendering || !pdfDoc || !context) return;
        rendering = true;

        pdfDoc.getPage(num).then(function (page) {
            const viewport = page.getViewport({scale: scale});
            pdfCanvas.height = viewport.height;
            pdfCanvas.width = viewport.width;

            pdfCanvas.style.opacity = 0;
            page.render({canvasContext: context, viewport: viewport}).promise.then(() => {
                pdfCanvas.style.opacity = 1;
                rendering = false;
            }).catch(err => {
                console.error('Render error:', err);
                rendering = false;
            });

            updatePageInfo();
        }).catch(err => {
            console.error('Page fetch error:', err);
            rendering = false;
        });

        preloadPage(num - 1);
        preloadPage(num + 1);
    }

    function preloadPage(num) {
        if (num >= 1 && num <= pageCount) {
            pdfDoc.getPage(num);
        }
    }

    function prevPage() {
        if (pageNum <= 1 || rendering) return;
        pageNum--;
        renderPage(pageNum);
    }

    function nextPage() {
        if (pageNum >= pageCount || rendering) return;
        pageNum++;
        renderPage(pageNum);
    }

    function goToPage(page) {
        const num = parseInt(page);
        if (num >= 1 && num <= pageCount && !rendering) {
            pageNum = num;
            renderPage(pageNum);
        }
    }

    function zoomIn() {
        if (rendering) return;
        scale += 0.25;
        renderPage(pageNum);
    }

    function zoomOut() {
        if (scale <= 0.5 || rendering) return;
        scale -= 0.25;
        renderPage(pageNum);
    }

    function toggleFullScreen() {
        const viewer = document.querySelector('.pdf-viewer');
        if (!document.fullscreenElement) {
            viewer.requestFullscreen().catch(err => console.log(`Error: ${err.message}`));
        } else {
            document.exitFullscreen();
        }
    }

    document.addEventListener('fullscreenchange', () => {
        if (document.fullscreenElement) {
            pdfCanvas.style.width = '100vw';
            pdfCanvas.style.height = '100vh';
        } else {
            pdfCanvas.style.width = '';
            pdfCanvas.style.height = '';
        }
        renderPage(pageNum);
    });

    pdfContainer?.addEventListener('dblclick', (e) => e.preventDefault());
</script>
</body>
</html>