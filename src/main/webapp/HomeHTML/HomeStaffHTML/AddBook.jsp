<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Account" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<%@ page import="dao.CategoryDAO" %>
<%@ page import="model.BookCategories" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Book</title>
    <!-- Bootstrap 5.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Font Awesome 6 -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">
    <!-- Animate.css -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Inter', sans-serif;
            background: linear-gradient(135deg, #f1f3f5, #d8dee9);
            min-height: 100vh;
            color: #212529;
            transition: all 0.5s ease;
            overflow-x: hidden;
        }

        body.dark-mode {
            background: linear-gradient(135deg, #2c3e50, #1a252f);
            color: #e9ecef;
        }

        body.custom-theme {
            background: linear-gradient(135deg, #ffecd2, #fcb69f);
            color: #4a2c2a;
        }

        .sidebar {
            position: fixed;
            top: 0;
            left: 0;
            width: 280px;
            height: 100%;
            background: linear-gradient(180deg, #007bff, #00a8ff);
            color: #fff;
            padding: 20px;
            box-shadow: 2px 0 15px rgba(0, 0, 0, 0.3);
            transition: width 0.3s ease;
            z-index: 1000;
        }

        .sidebar.dark-mode {
            background: linear-gradient(180deg, #2d3436, #636e72);
        }

        .sidebar.custom-theme {
            background: linear-gradient(180deg, #ff7e5f, #feb47b);
        }

        .sidebar.collapsed {
            width: 80px;
        }

        .sidebar .sidebar-brand {
            font-size: 1.8rem;
            font-weight: 700;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 30px;
        }

        .sidebar .nav-link {
            color: #fff;
            font-size: 1.1rem;
            padding: 15px;
            display: flex;
            align-items: center;
            gap: 10px;
            border-radius: 8px;
            transition: all 0.3s ease;
        }

        .sidebar .nav-link:hover {
            background: rgba(255, 255, 255, 0.2);
        }

        .sidebar.collapsed .nav-link span, .sidebar.collapsed .sub-menu {
            display: none;
        }

        .sidebar .sub-menu {
            margin-left: 20px;
            display: none;
        }

        .sidebar .nav-item.active .sub-menu {
            display: block;
        }

        .sidebar .dropdown-menu {
            background: #fff;
            border: none;
            border-radius: 8px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.15);
            min-width: 250px;
            position: absolute;
            left: 290px;
            top: 0;
        }

        .dark-mode .dropdown-menu {
            background: #343a40;
            color: #e9ecef;
        }

        .custom-theme .dropdown-menu {
            background: #fff3e0;
            color: #4a2c2a;
        }

        .dropdown-item {
            font-size: 1rem;
            padding: 10px 20px;
        }

        .dropdown-item:hover {
            background: #007bff;
            color: #fff;
        }

        .profile-info {
            padding: 15px 20px;
            border-bottom: 1px solid #dee2e6;
            font-size: 0.95rem;
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
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
            padding: 30px;
            max-width: 700px;
            margin: 0 auto;
            transition: all 0.3s ease;
        }

        .dark-mode .form-container {
            background: #343a40;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.4);
        }

        .custom-theme .form-container {
            background: #fff3e0;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
        }

        .form-container h2 {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 30px;
            text-align: center;
            color: #007bff;
        }

        .dark-mode .form-container h2 {
            color: #00a8ff;
        }

        .custom-theme .form-container h2 {
            color: #ff7e5f;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            font-weight: 600;
            margin-bottom: 8px;
            display: block;
            color: #495057;
        }

        .dark-mode .form-group label {
            color: #e9ecef;
        }

        .custom-theme .form-group label {
            color: #4a2c2a;
        }

        .form-group input, .form-group textarea, .form-group select {
            width: 100%;
            padding: 12px;
            border: 1px solid #ced4da;
            border-radius: 8px;
            font-size: 1rem;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-group input:focus, .form-group textarea:focus, .form-group select:focus {
            border-color: #007bff;
            box-shadow: 0 0 5px rgba(0, 123, 255, 0.5);
            outline: none;
        }

        .dark-mode .form-group input, .dark-mode .form-group textarea, .dark-mode .form-group select {
            background: #495057;
            border-color: #6c757d;
            color: #e9ecef;
        }

        .dark-mode .form-group input:focus, .dark-mode .form-group textarea:focus, .dark-mode .form-group select:focus {
            border-color: #00a8ff;
            box-shadow: 0 0 5px rgba(0, 168, 255, 0.5);
        }

        .custom-theme .form-group input, .custom-theme .form-group textarea, .custom-theme .form-group select {
            background: #fef9e7;
            border-color: #fcb69f;
            color: #4a2c2a;
        }

        .custom-theme .form-group input:focus, .custom-theme .form-group textarea:focus, .custom-theme .form-group select:focus {
            border-color: #ff7e5f;
            box-shadow: 0 0 5px rgba(255, 126, 95, 0.5);
        }

        .btn-submit {
            background: #007bff;
            color: #fff;
            padding: 12px 25px;
            border: none;
            border-radius: 8px;
            font-size: 1.1rem;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;
            display: block;
            width: 100%;
        }

        .btn-submit:hover {
            background: #0056b3;
            transform: translateY(-2px);
            box-shadow: 0 4px 10px rgba(0, 123, 255, 0.3);
        }

        .dark-mode .btn-submit {
            background: #00a8ff;
        }

        .dark-mode .btn-submit:hover {
            background: #007bbb;
            box-shadow: 0 4px 10px rgba(0, 168, 255, 0.3);
        }

        .custom-theme .btn-submit {
            background: #ff7e5f;
        }

        .custom-theme .btn-submit:hover {
            background: #e66a4e;
            box-shadow: 0 4px 10px rgba(255, 126, 95, 0.3);
        }

        .btn-back {
            background: #6c757d;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 8px;
            transition: background 0.3s ease;
        }

        .btn-back:hover {
            background: #5a6268;
        }

        #pdfUpload {
            display: none;
            margin-top: 15px;
        }

        @media (max-width: 768px) {
            .sidebar {
                width: 80px;
            }

            .main-content {
                margin-left: 80px;
            }

            .sidebar .nav-link span, .sidebar .sub-menu {
                display: none;
            }

            .form-container {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<%
    Account account = (Account) session.getAttribute("account");
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>

<!-- Sidebar -->
<div class="sidebar" id="sidebar">
    <div class="sidebar-brand">
        <span><i class="fas fa-book-open me-2"></i>Library</span>
        <i class="fas fa-bars toggle-btn" id="toggleSidebar"></i>
    </div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link" href="HomePage#book-management"><i class="fas fa-book"></i> <span>Books</span></a>
            <div class="sub-menu">
                <a class="nav-link" href="AddBook.jsp"><i class="fas fa-plus"></i> <span>Add Book</span></a>
                <a class="nav-link" href="ManageBooks.jsp"><i class="fas fa-edit"></i> <span>Manage</span></a>
                <a class="nav-link" href="DeleteBook.jsp"><i class="fas fa-trash-alt"></i> <span>Delete</span></a>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="HomePage#member-management"><i class="fas fa-users"></i>
                <span>Members</span></a>
            <div class="sub-menu">
                <a class="nav-link" href="BanMember.jsp"><i class="fas fa-ban"></i> <span>Ban</span></a>
                <a class="nav-link" href="UnbanMember.jsp"><i class="fas fa-check-circle"></i> <span>Unban</span></a>
                <a class="nav-link" href="UpdateMemberInfo.jsp"><i class="fas fa-user-edit"></i> <span>Update</span></a>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="HomePage#borrowing-history"><i class="fas fa-history"></i>
                <span>Borrowing History</span></a>
            <div class="sub-menu">
                <a class="nav-link" href="BorrowingHistory.jsp"><i class="fas fa-eye"></i> <span>View History</span></a>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="HomePage#fine-payment"><i class="fas fa-money-bill-alt"></i>
                <span>Fine & Payment</span></a>
            <div class="sub-menu">
                <a class="nav-link" href="ManageFines.jsp"><i class="fas fa-money-check-alt"></i>
                    <span>Manage Fines</span></a>
                <a class="nav-link" href="ViewPayments.jsp"><i class="fas fa-credit-card"></i>
                    <span>View Payments</span></a>
            </div>
        </li>
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="profileDropdown" role="button" data-bs-toggle="dropdown"
               aria-expanded="false">
                <i class="fas fa-user"></i> <span><%= account.getFullName() %></span>
            </a>
            <ul class="dropdown-menu" aria-labelledby="profileDropdown">
                <li class="profile-info">
                    <p><strong>Username:</strong> <%= account.getUsername() %>
                    </p>
                    <p><strong>Email:</strong> <%= account.getEmails() %>
                    </p>
                    <p><strong>Role ID:</strong> <%= account.getRoleId() %>
                    </p>
                    <p>
                        <strong>Created:</strong> <%= account.getCreatedAt() != null ? sdf.format(account.getCreatedAt()) : "N/A" %>
                    </p>
                </li>
                <li><a class="dropdown-item" href="#">Settings</a></li>
                <li><a class="dropdown-item" href="#" id="toggleLightMode">Light Mode</a></li>
                <li><a class="dropdown-item" href="#" id="toggleDarkMode">Dark Mode</a></li>
                <li><a class="dropdown-item" href="#" id="toggleCustomTheme">Custom Theme</a></li>
                <li><a class="dropdown-item" href="#">Logout</a></li>
            </ul>
        </li>
    </ul>
</div>

<!-- Main Content -->
<div class="main-content" id="mainContent">
    <div class="container-fluid">
        <div class="form-container animate__animated animate__fadeIn">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="mb-0">Add New Book</h2>
                <a href="HomePage" class="btn btn-back"><i class="fas fa-arrow-left me-2"></i>Back</a>
            </div>
            <form action="AddBook" method="POST" enctype="multipart/form-data" class="needs-validation" novalidate>
                <div class="form-group">
                    <label for="title">Title</label>
                    <input type="text" class="form-control" id="title" name="title" placeholder="Enter book title"
                           required>
                    <div class="invalid-feedback">Please enter a title.</div>
                </div>
                <div class="form-group">
                    <label for="author">Author</label>
                    <input type="text" class="form-control" id="author" name="author" placeholder="Enter author name"
                           required>
                    <div class="invalid-feedback">Please enter an author.</div>
                </div>
                <div class="form-group">
                    <label for="isbn">ISBN</label>
                    <input type="text" class="form-control" id="isbn" name="isbn" placeholder="Enter ISBN" required>
                    <div class="invalid-feedback">Please enter an ISBN.</div>
                </div>
                <div class="form-group">
                    <label for="publisher">Publisher</label>
                    <input type="text" class="form-control" id="publisher" name="publisher"
                           placeholder="Enter publisher">
                </div>
                <div class="form-group">
                    <label for="yearPublished">Year Published</label>
                    <input type="number" class="form-control" id="yearPublished" name="yearPublished"
                           placeholder="Enter year" min="1800" max="<%= java.time.Year.now().getValue() %>">
                </div>
                <div class="form-group">
                    <label for="categoryId">Category</label>
                    <select class="form-control" id="categoryId" name="categoryId" required>
                        <option value="" disabled selected>Select a category</option>
                        <%
                            try {
                                CategoryDAO categoryDAO = new CategoryDAO();
                                List<BookCategories> categories = categoryDAO.getAllCategories();
                                if (categories != null && !categories.isEmpty()) {
                                    for (BookCategories category : categories) {
                        %>
                        <option value="<%= category.getIdCategory() %>"><%= category.getCategoryName() %>
                        </option>
                        <%
                            }
                        } else {
                        %>
                        <option value="" disabled>No categories available</option>
                        <%
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        %>
                        <option value="" disabled>Error loading categories</option>
                        <%
                            }
                        %>
                    </select>
                    <div class="invalid-feedback">Please select a category.</div>
                </div>
                <div class="form-group">
                    <label for="copiesAvailable">Copies Available</label>
                    <input type="number" class="form-control" id="copiesAvailable" name="copiesAvailable"
                           placeholder="Enter number of copies" min="1" required>
                    <div class="invalid-feedback">Please enter the number of copies (minimum 1).</div>
                </div>
                <div class="form-group">
                    <label for="isDigital">Is Digital</label>
                    <select class="form-control" id="isDigital" name="isDigital">
                        <option value="false">No</option>
                        <option value="true">Yes</option>
                    </select>
                </div>
                <div class="form-group">
                    <label for="imageFile">Image File (PNG only)</label>
                    <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/png" required>
                    <div class="invalid-feedback">Please upload a PNG image.</div>
                </div>
                <div class="form-group" id="pdfUpload">
                    <label for="pdfFile">PDF File (for digital books, max 100MB)</label>
                    <input type="file" class="form-control" id="pdfFile" name="pdfFile" accept=".pdf">
                    <div class="invalid-feedback">Please upload a PDF file for digital books.</div>
                </div>
                <div class="form-group">
                    <label for="description">Description</label>
                    <textarea class="form-control" id="description" name="description" rows="4"
                              placeholder="Enter book description"></textarea>
                </div>
                <button type="submit" class="btn-submit">Add Book</button>
            </form>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script>
    // Sidebar Toggle
    document.getElementById('toggleSidebar').addEventListener('click', () => {
        const sidebar = document.getElementById('sidebar');
        const mainContent = document.getElementById('mainContent');
        sidebar.classList.toggle('collapsed');
        mainContent.classList.toggle('expanded');
    });

    // Sub-menu Toggle
    document.querySelectorAll('.nav-item > .nav-link:not(.dropdown-toggle)').forEach(item => {
        item.addEventListener('click', () => {
            const parent = item.parentElement;
            parent.classList.toggle('active');
        });
    });

    // Theme Switcher
    const setTheme = (theme) => {
        document.body.classList.remove('dark-mode', 'custom-theme');
        document.querySelector('.sidebar').classList.remove('dark-mode', 'custom-theme');
        document.querySelector('.form-container').classList.remove('dark-mode', 'custom-theme');
        if (theme === 'dark') {
            document.body.classList.add('dark-mode');
            document.querySelector('.sidebar').classList.add('dark-mode');
            document.querySelector('.form-container').classList.add('dark-mode');
        } else if (theme === 'custom') {
            document.body.classList.add('custom-theme');
            document.querySelector('.sidebar').classList.add('custom-theme');
            document.querySelector('.form-container').classList.add('custom-theme');
        }
        localStorage.setItem('theme', theme);
    };

    document.getElementById('toggleLightMode').addEventListener('click', () => setTheme('light'));
    document.getElementById('toggleDarkMode').addEventListener('click', () => setTheme('dark'));
    document.getElementById('toggleCustomTheme').addEventListener('click', () => setTheme('custom'));

    if (localStorage.getItem('theme')) {
        setTheme(localStorage.getItem('theme'));
    }

    // Show/Hide PDF upload based on isDigital
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
    togglePdfUpload(); // Gọi ngay khi tải trang để đồng bộ trạng thái ban đầu

    // Bootstrap form validation
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