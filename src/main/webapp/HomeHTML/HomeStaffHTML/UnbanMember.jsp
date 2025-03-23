<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Account" %>
<%@ page import="dao.MembersDAO" %>
<%@ page import="model.Members" %>
<%@ page import="java.util.List" %>
<%@ page import="java.text.SimpleDateFormat" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Unban Member</title>
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
        /* CSS giống BanMember.jsp */
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

        .btn-info {
            background: #17a2b8;
        }

        .btn-dark {
            background: #343a40;
        }

        .table-container {
            margin-top: 20px;
        }

        .table th {
            background: #007bff;
            color: #fff;
        }

        .dark-mode .table th {
            background: #2d3436;
        }

        .custom-theme .table th {
            background: #ff7e5f;
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
    MembersDAO membersDAO = new MembersDAO();
    List<Members> membersList = membersDAO.getAllMembers();
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
            <a class="nav-link" href="HomePage#member-management"><i class="fas fa-users"></i>
                <span>Members</span></a>
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
        <li class="nav-item">
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
        <h1 class="dashboard-title">Unban Member</h1>

        <!-- Messages -->
        <% String message = (String) request.getAttribute("message"); %>
        <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
        <% if (message != null) { %>
        <div class="alert alert-success"><i class="fas fa-check-circle me-2"></i><%= message %>
        </div>
        <% } %>
        <% if (errorMessage != null) { %>
        <div class="alert alert-danger"><i class="fas fa-exclamation-triangle me-2"></i><%= errorMessage %>
        </div>
        <% } %>

        <div class="row">
            <!-- Unban Member Form -->
            <div class="col-lg-6">
                <div class="card" id="unban-member">
                    <div class="card-header">
                        <i class="fas fa-check-circle"></i> Unban a Member
                    </div>
                    <div class="card-body">
                        <form action="UnbanMember" method="post" id="unbanForm">
                            <div class="mb-3">
                                <label for="memberId" class="form-label"><i class="fas fa-id-card me-2"></i>Member ID
                                    (Optional)</label>
                                <input type="number" id="memberId" name="memberId" class="form-control"
                                       placeholder="Enter Member ID">
                            </div>
                            <div class="mb-3">
                                <label for="email" class="form-label"><i class="fas fa-envelope me-2"></i>Email
                                    (Optional)</label>
                                <input type="email" id="email" name="email" class="form-control"
                                       placeholder="Enter Member Email">
                            </div>
                            <button type="submit" class="btn btn-info btn-custom"><i
                                    class="fas fa-check-circle me-2"></i>Unban Member
                            </button>
                            <a href="HomePage" class="btn btn-dark btn-custom"><i class="fas fa-arrow-left me-2"></i>Back</a>
                        </form>
                    </div>
                </div>
            </div>

            <!-- Members List -->
            <div class="col-lg-6">
                <div class="card" id="members-list">
                    <div class="card-header">
                        <i class="fas fa-users"></i> Members List
                    </div>
                    <div class="card-body">
                        <div class="table-container">
                            <table class="table table-striped">
                                <thead>
                                <tr>
                                    <th>ID</th>
                                    <th>Full Name</th>
                                    <th>Email</th>
                                    <th>Status</th>
                                </tr>
                                </thead>
                                <tbody>
                                <% for (Members member : membersList) { %>
                                <tr>
                                    <td><%= member.getIdMember() %>
                                    </td>
                                    <td><%= member.getFullName() %>
                                    </td>
                                    <td><%= member.getEmail() %>
                                    </td>
                                    <td><%= member.getStatus() %>
                                    </td>
                                </tr>
                                <% } %>
                                </tbody>
                            </table>
                        </div>
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

    // Form Validation
    document.getElementById('unbanForm')?.addEventListener('submit', function (e) {
        const memberId = document.getElementById('memberId').value;
        const email = document.getElementById('email').value;
        if (!memberId && !email) {
            e.preventDefault();
            alert('Please provide either a Member ID or an Email.');
        }
    });
</script>
</body>
</html>