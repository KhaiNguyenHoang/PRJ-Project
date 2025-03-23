<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Account" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="java.util.List" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manage Staff</title>
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

        .table-container {
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.12);
            padding: 30px;
            transition: all 0.3s ease;
        }

        .dark-mode .table-container {
            background: #343a40;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.4);
        }

        .custom-theme .table-container {
            background: #fff3e0;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
        }

        .table-container h2 {
            font-size: 2rem;
            font-weight: 700;
            margin-bottom: 30px;
            text-align: center;
            color: #007bff;
        }

        .dark-mode .table-container h2 {
            color: #00a8ff;
        }

        .custom-theme .table-container h2 {
            color: #ff7e5f;
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

        .table-container .table {
            margin-bottom: 0;
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

            .table-container {
                padding: 20px;
            }
        }
    </style>
</head>
<body>
<%
    Account account = (Account) session.getAttribute("account");
    if (account == null || account.getRoleId() != 1) {
        response.sendRedirect("HomePage");
        return;
    }
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
%>
<!-- Sidebar -->
<div class="sidebar" id="sidebar">
    <div class="sidebar-brand">
        <span><i class="fas fa-crown me-2"></i>Admin</span>
        <i class="fas fa-bars toggle-btn" id="toggleSidebar"></i>
    </div>
    <ul class="nav flex-column">
        <li class="nav-item">
            <a class="nav-link" href="HomePage#dashboard-overview"><i class="fas fa-tachometer-alt"></i>
                <span>Overview</span></a>
        </li>
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
            <a class="nav-link" href="HomePage#borrowing-history"><i class="fas fa-history"></i> <span>Borrowing</span></a>
            <div class="sub-menu">
                <a class="nav-link" href="BorrowingHistory"><i class="fas fa-eye"></i> <span>View History</span></a>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="HomePage#fine-payment"><i class="fas fa-money-bill-alt"></i>
                <span>Fines</span></a>
            <div class="sub-menu">
                <a class="nav-link" href="StaffManageFine"><i class="fas fa-money-check-alt"></i>
                    <span>Manage Fines</span></a>
            </div>
        </li>
        <li class="nav-item">
            <a class="nav-link" href="HomePage#staff-management"><i class="fas fa-user-tie"></i>
                <span>Staff</span></a>
            <div class="sub-menu">
                <a class="nav-link" href="AddStaff"><i class="fas fa-plus"></i> <span>Add Staff</span></a>
                <a class="nav-link" href="ManageStaff"><i class="fas fa-edit"></i> <span>Manage Staff</span></a>
                <a class="nav-link" href="DeleteStaff"><i class="fas fa-trash-alt"></i> <span>Delete Staff</span></a>
            </div>
        </li>
        <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="profileDropdown" role="button" data-bs-toggle="dropdown"
               aria-expanded="false">
                <i class="fas fa-user-shield"></i> <span><%= account.getFullName() %></span>
            </a>
            <ul class="dropdown-menu" aria-labelledby="profileDropdown">
                <li class="profile-info">
                    <p><strong>Username:</strong> <%= account.getUsername() %>
                    </p>
                    <p><strong>Email:</strong> <%= account.getEmails() %>
                    </p>
                    <p>
                        <strong>Created:</strong> <%= account.getCreatedAt() != null ? new java.text.SimpleDateFormat("dd/MM/yyyy").format(account.getCreatedAt()) : "N/A" %>
                    </p>
                </li>
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
        <div class="table-container animate__animated animate__fadeIn">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="mb-0">Manage Staff</h2>
                <a href="HomePage" class="btn btn-back"><i class="fas fa-arrow-left me-2"></i>Back</a>
            </div>
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
                    <th>Full Name</th>
                    <th>Email</th>
                    <th>Username</th>
                    <th>Role</th>
                    <th>Created At</th>
                    <th>Actions</th>
                </tr>
                </thead>
                <tbody>
                <% List<Account> staffList = (List<Account>) request.getAttribute("staffList");
                    if (staffList != null && !staffList.isEmpty()) {
                        for (Account staff : staffList) { %>
                <tr>
                    <td><%= staff.getIdAccount() %>
                    </td>
                    <td><%= staff.getFullName() %>
                    </td>
                    <td><%= staff.getEmails() %>
                    </td>
                    <td><%= staff.getUsername() %>
                    </td>
                    <td><%= staff.getRoleId() == 1 ? "Admin" : "Staff" %>
                    </td>
                    <td><%= staff.getCreatedAt() != null ? sdf.format(staff.getCreatedAt()) : "N/A" %>
                    </td>
                    <td>
                        <a href="ManageStaff?action=edit&id=<%= staff.getIdAccount() %>" class="btn btn-primary btn-sm">
                            <i class="fas fa-edit"></i> Edit
                        </a>
                    </td>
                </tr>
                <% }
                } else { %>
                <tr>
                    <td colspan="7" class="text-center">No staff found.</td>
                </tr>
                <% } %>
                </tbody>
            </table>
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
        document.querySelectorAll('.table-container').forEach(container => {
            container.classList.remove('dark-mode', 'custom-theme');
        });
        if (theme === 'dark') {
            document.body.classList.add('dark-mode');
            document.querySelector('.sidebar').classList.add('dark-mode');
            document.querySelectorAll('.table-container').forEach(container => {
                container.classList.add('dark-mode');
            });
        } else if (theme === 'custom') {
            document.body.classList.add('custom-theme');
            document.querySelector('.sidebar').classList.add('custom-theme');
            document.querySelectorAll('.table-container').forEach(container => {
                container.classList.add('custom-theme');
            });
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