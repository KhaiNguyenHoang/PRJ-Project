<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Account" %>
<%@ page import="model.Fines" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Staff - Manage Fines</title>
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

        .dashboard-title {
            font-size: 2.8rem;
            font-weight: 700;
            text-align: center;
            margin-bottom: 50px;
            animation: fadeInDown 1s ease;
        }

        .card {
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
            transition: all 0.3s ease;
            margin-bottom: 30px;
            overflow: hidden;
            opacity: 0;
        }

        .card.loaded {
            opacity: 1;
            animation: fadeIn 0.7s ease-in-out;
        }

        .dark-mode .card {
            background: #343a40;
        }

        .custom-theme .card {
            background: #fff3e0;
        }

        .card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 30px rgba(0, 0, 0, 0.2);
        }

        .card-header {
            background: linear-gradient(90deg, #007bff, #00a8ff);
            color: #fff;
            padding: 20px;
            font-size: 1.5rem;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 10px;
        }

        .dark-mode .card-header {
            background: linear-gradient(90deg, #2d3436, #636e72);
        }

        .custom-theme .card-header {
            background: linear-gradient(90deg, #ff7e5f, #feb47b);
        }

        .card-body {
            padding: 30px;
            background: #f8f9fa;
        }

        .dark-mode .card-body {
            background: #495057;
        }

        .custom-theme .card-body {
            background: #fef9e7;
        }

        .btn-custom {
            display: flex;
            align-items: center;
            gap: 10px;
            width: 100%;
            padding: 15px 25px;
            margin-bottom: 15px;
            font-size: 1.1rem;
            font-weight: 500;
            border-radius: 8px;
            color: #fff;
            text-decoration: none;
            transition: all 0.3s ease;
        }

        .btn-custom:hover {
            transform: scale(1.03);
            filter: brightness(1.15);
        }

        .btn-secondary {
            background: #6c757d;
        }

        .btn-dark {
            background: #343a40;
        }

        .btn-table {
            padding: 5px 15px;
            font-size: 0.9rem;
            border-radius: 5px;
            white-space: nowrap;
        }

        .table-container {
            margin-top: 20px;
            overflow-x: auto; /* Hỗ trợ cuộn ngang trên màn hình nhỏ */
        }

        .table {
            width: 100%;
            min-width: 1200px; /* Đảm bảo bảng đủ rộng để hiển thị tất cả cột */
        }

        .table th {
            background: #007bff;
            color: #fff;
            font-weight: 600;
            padding: 12px;
        }

        .dark-mode .table th {
            background: #2d3436;
        }

        .custom-theme .table th {
            background: #ff7e5f;
        }

        .table th, .table td {
            vertical-align: middle;
            white-space: nowrap; /* Ngăn xuống dòng */
            padding: 10px 15px;
            text-align: center; /* Căn giữa nội dung */
        }

        .table td select {
            width: 120px; /* Độ rộng cố định cho select */
            padding: 5px;
        }

        .table td form {
            display: flex;
            gap: 5px;
            justify-content: center;
            align-items: center;
        }

        .loading-overlay {
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            display: flex;
            justify-content: center;
            align-items: center;
            z-index: 3000;
            transition: opacity 0.5s ease;
        }

        .spinner {
            width: 60px;
            height: 60px;
            border: 6px solid #fff;
            border-top: 6px solid #007bff;
            border-radius: 50%;
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

        @keyframes fadeIn {
            from {
                opacity: 0;
            }
            to {
                opacity: 1;
            }
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

            .dashboard-title {
                font-size: 2rem;
            }

            .table td select {
                width: 100px; /* Thu nhỏ select trên mobile */
            }

            .btn-table {
                padding: 4px 10px;
                font-size: 0.8rem;
            }
        }
    </style>
</head>
<body>
<%
    Account account = (Account) session.getAttribute("account");
    if (account == null) {
        response.sendRedirect("/Auth/SignIn-SignUp.jsp");
        return;
    }
%>
<!-- Loading Overlay -->
<div class="loading-overlay" id="loadingOverlay">
    <div class="spinner"></div>
</div>
<%
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
                <a class="nav-link" href="AddBook"><i class="fas fa-plus"></i> <span>Add Book</span></a>
                <a class="nav-link" href="ManageBook"><i class="fas fa-edit"></i> <span>Manage</span></a>
                <a class="nav-link" href="DeleteBook"><i class="fas fa-trash-alt"></i> <span>Delete</span></a>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="HomePage#member-management"><i class="fas fa-users"></i> <span>Members</span></a>
            <div class="sub-menu">
                <a class="nav-link" href="BanMember"><i class="fas fa-ban"></i> <span>Ban</span></a>
                <a class="nav-link" href="UnbanMember"><i class="fas fa-check-circle"></i> <span>Unban</span></a>
                <a class="nav-link" href="UpdateMember"><i class="fas fa-user-edit"></i> <span>Update</span></a>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="HomePage#borrowing-history"><i class="fas fa-history"></i>
                <span>Borrowing History</span></a>
            <div class="sub-menu">
                <a class="nav-link" href="BorrowingHistory"><i class="fas fa-eye"></i> <span>View History</span></a>
            </div>
        </li>
        <li class="nav-item active">
            <a class="nav-link" href="HomePage#fine-payment"><i class="fas fa-money-bill-alt"></i>
                <span>Fine & Payment</span></a>
            <div class="sub-menu">
                <a class="nav-link" href="StaffManageFine"><i class="fas fa-money-check-alt"></i>
                    <span>Manage Fines</span></a>
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
                    <p>
                        <strong>Created:</strong> <%= account.getCreatedAt() != null ? sdf.format(account.getCreatedAt()) : "N/A" %>
                    </p>
                </li>
                <li><a class="dropdown-item" href="#">Settings</a></li>
                <li><a class="dropdown-item" href="#" id="toggleLightMode">Light Mode</a></li>
                <li><a class="dropdown-item" href="#" id="toggleDarkMode">Dark Mode</a></li>
                <li><a class="dropdown-item" href="#" id="toggleCustomTheme">Custom Theme</a></li>
                <li><a class="dropdown-item" href="logout">Logout</a></li>
            </ul>
        </li>
    </ul>
</div>

<!-- Main Content -->
<div class="main-content" id="mainContent">
    <div class="container-fluid">
        <h1 class="dashboard-title">Manage Fines</h1>

        <!-- Messages -->
        <% String message = (String) request.getAttribute("message"); %>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% String infoMessage = (String) request.getAttribute("infoMessage"); %>
        <% if (message != null) { %>
        <div class="alert alert-success"><i class="fas fa-check-circle me-2"></i><%= message %>
        </div>
        <% } %>
        <% if (errorMessage != null) { %>
        <div class="alert alert-danger"><i class="fas fa-exclamation-triangle me-2"></i><%= errorMessage %>
        </div>
        <% } %>
        <% if (infoMessage != null) { %>
        <div class="alert alert-info"><i class="fas fa-info-circle me-2"></i><%= infoMessage %>
        </div>
        <% } %>

        <div class="row">
            <!-- Fines List -->
            <div class="col-12">
                <div class="card" id="fines-list">
                    <div class="card-header">
                        <i class="fas fa-money-bill-wave"></i> Fines List
                    </div>
                    <div class="card-body">
                        <!-- Search and Filter -->
                        <form action="StaffManageFine" method="get" class="mb-4">
                            <div class="row">
                                <div class="col-md-4 mb-3">
                                    <label for="status" class="form-label"><i class="fas fa-filter me-2"></i>Filter by
                                        Status</label>
                                    <select id="status" name="status" class="form-select">
                                        <option value="All" <%= "All".equals(request.getAttribute("filterStatus")) ? "selected" : "" %>>
                                            All
                                        </option>
                                        <option value="Unpaid" <%= "Unpaid".equals(request.getAttribute("filterStatus")) ? "selected" : "" %>>
                                            Unpaid
                                        </option>
                                        <option value="Paid" <%= "Paid".equals(request.getAttribute("filterStatus")) ? "selected" : "" %>>
                                            Paid
                                        </option>
                                    </select>
                                </div>
                                <div class="col-md-2 mb-3 d-flex align-items-end">
                                    <button type="submit" class="btn btn-secondary btn-custom w-100"><i
                                            class="fas fa-search me-2"></i>Search
                                    </button>
                                </div>
                            </div>
                        </form>

                        <!-- Fines Table -->
                        <div class="table-container">
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>Fine ID</th>
                                    <th>Member ID</th>
                                    <th>Borrow ID</th>
                                    <th>Amount</th>
                                    <th>Status</th>
                                    <th>Created At</th>
                                    <th>Paid At</th>
                                    <th>Payment Method</th>
                                    <th>Action</th>
                                    <th>Pay</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% List<Fines> finesList = (List<Fines>) request.getAttribute("finesList"); %>
                                <% SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm"); %>
                                <% if (finesList != null && !finesList.isEmpty()) { %>
                                <% for (Fines fine : finesList) { %>
                                <tr>
                                    <td><%= fine.getIdFine() %>
                                    </td>
                                    <td><%= fine.getMemberId() %>
                                    </td>
                                    <td><%= fine.getBorrowId() %>
                                    </td>
                                    <td><%= String.format("%.2f", fine.getAmount()) %> USD</td>
                                    <td><%= fine.getStatus() %>
                                    </td>
                                    <td><%= dateFormat.format(fine.getCreatedAt()) %>
                                    </td>
                                    <td><%= fine.getPaidDate() != null ? dateFormat.format(fine.getPaidDate()) : "N/A" %>
                                    </td>
                                    <td><%= fine.getPaymentMethod() != null ? fine.getPaymentMethod() : "N/A" %>
                                    </td>
                                    <td>
                                        <% if ("Unpaid".equalsIgnoreCase(fine.getStatus())) { %>
                                        <span class="text-danger"><i
                                                class="fas fa-exclamation-circle me-1"></i>Unpaid</span>
                                        <% } else { %>
                                        <span class="text-success"><i class="fas fa-check-circle me-1"></i>Paid</span>
                                        <% } %>
                                    </td>
                                    <td>
                                        <% if ("Unpaid".equalsIgnoreCase(fine.getStatus())) { %>
                                        <form action="StaffManageFine" method="post" class="payment-form">
                                            <input type="hidden" name="fineId" value="<%= fine.getIdFine() %>">
                                            <input type="hidden" name="action" value="confirmPayment">
                                            <input type="hidden" name="search"
                                                   value="<%= request.getAttribute("searchKeyword") != null ? request.getAttribute("searchKeyword") : "" %>">
                                            <input type="hidden" name="status"
                                                   value="<%= request.getAttribute("filterStatus") != null ? request.getAttribute("filterStatus") : "" %>">
                                            <select name="paymentMethod" class="form-select" required>
                                                <option value="" disabled selected>Select</option>
                                                <option value="Cash">Cash</option>
                                                <option value="Credit Card">Credit Card</option>
                                                <option value="Online">Online</option>
                                                <option value="Other">Other</option>
                                            </select>
                                            <button type="submit" class="btn btn-secondary btn-table"><i
                                                    class="fas fa-money-check-alt me-1"></i>Pay
                                            </button>
                                        </form>
                                        <% } else { %>
                                        <span class="text-muted">-</span>
                                        <% } %>
                                    </td>
                                </tr>
                                <% } %>
                                <% } else { %>
                                <tr>
                                    <td colspan="10" class="text-center">No fines available.</td>
                                </tr>
                                <% } %>
                                </tbody>
                            </table>
                        </div>
                        <a href="HomePage" class="btn btn-dark btn-custom mt-3"><i class="fas fa-arrow-left me-2"></i>Back</a>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Scripts -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz"
        crossorigin="anonymous"></script>
<script>
    // Loading Overlay
    window.addEventListener('load', () => {
        setTimeout(() => {
            document.getElementById('loadingOverlay').style.opacity = '0';
            setTimeout(() => {
                document.getElementById('loadingOverlay').style.display = 'none';
                document.querySelectorAll('.card').forEach(card => card.classList.add('loaded'));
            }, 500);
        }, 1000);
    });

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
        document.querySelectorAll('.card').forEach(card => card.classList.remove('dark-mode', 'custom-theme'));
        if (theme === 'dark') {
            document.body.classList.add('dark-mode');
            document.querySelector('.sidebar').classList.add('dark-mode');
            document.querySelectorAll('.card').forEach(card => card.classList.add('dark-mode'));
        } else if (theme === 'custom') {
            document.body.classList.add('custom-theme');
            document.querySelector('.sidebar').classList.add('custom-theme');
            document.querySelectorAll('.card').forEach(card => card.classList.add('custom-theme'));
        }
        localStorage.setItem('theme', theme);
    };

    document.getElementById('toggleLightMode').addEventListener('click', () => setTheme('light'));
    document.getElementById('toggleDarkMode').addEventListener('click', () => setTheme('dark'));
    document.getElementById('toggleCustomTheme').addEventListener('click', () => setTheme('custom'));

    if (localStorage.getItem('theme')) {
        setTheme(localStorage.getItem('theme'));
    }
</script>
</body>
</html>