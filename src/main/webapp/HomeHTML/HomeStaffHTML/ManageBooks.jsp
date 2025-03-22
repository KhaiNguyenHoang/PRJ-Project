<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Account" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.CategoryDAO" %>
<%@ page import="model.BookCategories" %>
<%@ page import="model.Books" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Books</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Font Awesome Icons -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <!-- Animate.css -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css" rel="stylesheet">
    <style>
        body {
            font-family: 'Inter', sans-serif;
            background: #f1f3f5;
            overflow-x: hidden;
        }

        .sidebar {
            position: fixed;
            top: 0;
            left: 0;
            width: 280px;
            height: 100%;
            background: #007bff;
            color: #fff;
            padding: 20px;
            transition: width 0.3s ease;
        }

        .sidebar.collapsed {
            width: 80px;
        }

        .sidebar .nav-link {
            color: #fff;
            padding: 10px;
            border-radius: 5px;
        }

        .sidebar .nav-link:hover {
            background: #0056b3;
        }

        .main-content {
            margin-left: 280px;
            padding: 40px;
            transition: margin-left 0.3s ease;
        }

        .main-content.expanded {
            margin-left: 80px;
        }

        .form-container {
            background: #fff;
            border-radius: 15px;
            padding: 30px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            max-width: 900px;
            margin: 0 auto;
        }

        .table-container {
            background: #fff;
            border-radius: 15px;
            padding: 20px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .btn-submit {
            background: #007bff;
            color: #fff;
            padding: 12px;
            width: 100%;
            border: none;
            border-radius: 8px;
            transition: background 0.3s ease;
        }

        .btn-submit:hover {
            background: #0056b3;
        }

        .readonly {
            background-color: #e9ecef;
        }

        #pdfUpload {
            display: none;
            margin-top: 15px;
            transition: all 0.3s ease;
        }

        .alert {
            margin-bottom: 20px;
        }
    </style>
</head>
<body>
<%
    Account account = (Account) session.getAttribute("account");
    if (account == null) {
        account = new Account(1, "John Doe", "john.doe@example.com", "johndoe", "hashedpassword", 1);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    String action = request.getParameter("action");
%>

<!-- Sidebar -->
<div class="sidebar" id="sidebar">
    <div class="sidebar-brand d-flex justify-content-between align-items-center mb-4">
        <span class="fs-4"><i class="fas fa-book-open me-2"></i>Library</span>
        <i class="fas fa-bars toggle-btn" id="toggleSidebar" style="cursor: pointer;"></i>
    </div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link" href="ManageBook"><i class="fas fa-book me-2"></i> <span>Manage Books</span></a>
        </li>
        <!-- Thêm các mục khác nếu cần -->
    </ul>
</div>

<!-- Main Content -->
<div class="main-content" id="mainContent">
    <div class="container-fluid">
        <% if (action == null || action.equals("list")) { %>
        <div class="table-container animate__animated animate__fadeIn">
            <h2 class="mb-4">Manage Books</h2>
            <% String success = (String) session.getAttribute("success");
                if (success != null) { %>
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <%= success %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% session.removeAttribute("success"); %>
            <% } %>
            <% String error = (String) request.getAttribute("error");
                if (error != null) { %>
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <%= error %>
                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
            </div>
            <% } %>
            <table class="table table-striped table-hover">
                <thead class="table-dark">
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>ISBN</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% List<Books> books = (List<Books>) request.getAttribute("books");
                    if (books != null) {
                        for (Books book : books) { %>
                <tr>
                    <td><%= book.getIdBook() %>
                    </td>
                    <td><%= book.getTitle() %>
                    </td>
                    <td><%= book.getAuthor() %>
                    </td>
                    <td><%= book.getIsbn() %>
                    </td>
                    <td>
                        <a href="ManageBook?action=edit&id=<%= book.getIdBook() %>" class="btn btn-primary btn-sm"><i
                                class="fas fa-edit"></i> Edit</a>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="5" class="text-center">No books available.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
        </div>
        <% } else if (action.equals("edit")) { %>
        <div class="form-container animate__animated animate__fadeIn">
            <h2 class="mb-4">Edit Book</h2>
            <% Books book = (Books) request.getAttribute("book"); %>
            <% if (book != null) { %>
            <form action="ManageBook" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                <input type="hidden" name="id" value="<%= book.getIdBook() %>">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="idBook" class="form-label">ID</label>
                        <input type="text" class="form-control readonly" id="idBook" value="<%= book.getIdBook() %>"
                               disabled>
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="isbn" class="form-label">ISBN</label>
                        <input type="text" class="form-control readonly" id="isbn" value="<%= book.getIsbn() %>"
                               disabled>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="title" class="form-label">Title</label>
                    <input type="text" class="form-control" id="title" name="title" value="<%= book.getTitle() %>"
                           required>
                    <div class="invalid-feedback">Please enter a title.</div>
                </div>
                <div class="mb-3">
                    <label for="author" class="form-label">Author</label>
                    <input type="text" class="form-control" id="author" name="author" value="<%= book.getAuthor() %>"
                           required>
                    <div class="invalid-feedback">Please enter an author.</div>
                </div>
                <div class="mb-3">
                    <label for="publisher" class="form-label">Publisher</label>
                    <input type="text" class="form-control" id="publisher" name="publisher"
                           value="<%= book.getPublisher() %>">
                </div>
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <label for="yearPublished" class="form-label">Year Published</label>
                        <input type="number" class="form-control" id="yearPublished" name="yearPublished"
                               value="<%= book.getYearPublished() %>" min="1800"
                               max="<%= java.time.Year.now().getValue() %>">
                    </div>
                    <div class="col-md-6 mb-3">
                        <label for="copiesAvailable" class="form-label">Copies Available</label>
                        <input type="number" class="form-control readonly" id="copiesAvailable"
                               value="<%= book.getCopiesAvailable() %>" disabled>
                    </div>
                </div>
                <div class="mb-3">
                    <label for="categoryId" class="form-label">Category</label>
                    <select class="form-select" id="categoryId" name="categoryId" required>
                        <% List<BookCategories> categories = (List<BookCategories>) request.getAttribute("categories");
                            if (categories != null) {
                                for (BookCategories category : categories) { %>
                        <option value="<%= category.getIdCategory() %>" <%= category.getIdCategory() == book.getCategoryId() ? "selected" : "" %>><%= category.getCategoryName() %>
                        </option>
                        <% }
                        } %>
                    </select>
                    <div class="invalid-feedback">Please select a category.</div>
                </div>
                <div class="mb-3">
                    <label for="isDigital" class="form-label">Is Digital</label>
                    <select class="form-select" id="isDigital" name="isDigital">
                        <option value="false" <%= !book.isDigital() ? "selected" : "" %>>No</option>
                        <option value="true" <%= book.isDigital() ? "selected" : "" %>>Yes</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="status" class="form-label">Status</label>
                    <select class="form-select" id="status" name="status">
                        <option value="Available" <%= "Available".equals(book.getStatus()) ? "selected" : "" %>>
                            Available
                        </option>
                        <option value="Borrowed" <%= "Borrowed".equals(book.getStatus()) ? "selected" : "" %>>Borrowed
                        </option>
                        <option value="Reserved" <%= "Reserved".equals(book.getStatus()) ? "selected" : "" %>>Reserved
                        </option>
                        <option value="Lost" <%= "Lost".equals(book.getStatus()) ? "selected" : "" %>>Lost</option>
                    </select>
                </div>
                <div class="mb-3">
                    <label for="imageFile" class="form-label">Image File (PNG only,
                        current: <%= book.getFilePath() != null ? book.getFilePath() : "None" %>)</label>
                    <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/png">
                </div>
                <div class="mb-3" id="pdfUpload" style="<%= book.isDigital() ? "display: block;" : "display: none;" %>">
                    <label for="pdfFile" class="form-label">PDF File (max 100MB,
                        current: <%= book.getBookDetail() != null && book.getBookDetail().getPdfPath() != null ? book.getBookDetail().getPdfPath() : "None" %>
                        )</label>
                    <input type="file" class="form-control" id="pdfFile" name="pdfFile"
                           accept=".pdf" <%= book.isDigital() ? "required" : "" %>>
                    <div class="invalid-feedback">Please upload a PDF file for digital books.</div>
                </div>
                <div class="mb-3">
                    <label for="description" class="form-label">Description</label>
                    <textarea class="form-control" id="description" name="description"
                              rows="4"><%= book.getBookDetail() != null && book.getBookDetail().getDescription() != null ? book.getBookDetail().getDescription() : "" %></textarea>
                </div>
                <button type="submit" class="btn btn-submit">Update Book</button>
            </form>
            <% } else { %>
            <div class="alert alert-danger" role="alert">
                Book not found.
            </div>
            <% } %>
        </div>
        <% } %>
    </div>
</div>

<!-- Bootstrap 5 JS Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Toggle sidebar
    document.getElementById('toggleSidebar').addEventListener('click', () => {
        document.getElementById('sidebar').classList.toggle('collapsed');
        document.getElementById('mainContent').classList.toggle('expanded');
    });

    // Toggle PDF upload section based on isDigital
    const isDigitalSelect = document.getElementById('isDigital');
    const pdfUploadDiv = document.getElementById('pdfUpload');
    const pdfFileInput = document.getElementById('pdfFile');

    function togglePdfUpload() {
        if (isDigitalSelect.value === 'true') {
            pdfUploadDiv.style.display = 'block';
            pdfFileInput.setAttribute('required', 'true');
        } else {
            pdfUploadDiv.style.display = 'none';
            pdfFileInput.removeAttribute('required');
        }
    }

    isDigitalSelect.addEventListener('change', togglePdfUpload);
    togglePdfUpload();

    // Form validation
    (function () {
        'use strict';
        const forms = document.querySelectorAll('.needs-validation');
        Array.prototype.slice.call(forms).forEach(function (form) {
            form.addEventListener('submit', function (event) {
                if (!form.checkValidity()) {
                    event.preventDefault();
                    event.stopPropagation();
                }
                form.classList.add('was-validated');
            }, false);
        });
    })();
</script>
</body>
</html>