<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="model.Account" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Add New Staff</title>
    <!-- Bootstrap 5.3 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" crossorigin="anonymous">
    <!-- Font Awesome 6 -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" rel="stylesheet">
    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">
    <!-- Animate.css -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css" rel="stylesheet">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Roboto', sans-serif;
            background: linear-gradient(135deg, #e9ecef, #ced4da);
            min-height: 100vh;
            color: #343a40;
            overflow-x: hidden;
            transition: all 0.5s ease;
        }

        body.dark-mode {
            background: linear-gradient(135deg, #212529, #343a40);
            color: #f8f9fa;
        }

        body.custom-theme {
            background: linear-gradient(135deg, #ffafbd, #ffc3a0);
            color: #4b2e2e;
        }

        /* Sidebar */
        .sidebar {
            position: fixed;
            top: 0;
            left: 0;
            width: 300px;
            height: 100%;
            background: linear-gradient(180deg, #1e3a8a, #3b82f6);
            color: #fff;
            padding: 30px 20px;
            box-shadow: 4px 0 20px rgba(0, 0, 0, 0.2);
            transition: width 0.3s ease, transform 0.3s ease;
            z-index: 1000;
            overflow-y: auto;
        }

        .sidebar.dark-mode {
            background: linear-gradient(180deg, #111827, #374151);
        }

        .sidebar.custom-theme {
            background: linear-gradient(180deg, #ff6b6b, #feb2b2);
        }

        .sidebar.collapsed {
            width: 90px;
        }

        .sidebar .sidebar-brand {
            font-size: 2rem;
            font-weight: 700;
            color: #fff;
            display: flex;
            align-items: center;
            justify-content: space-between;
            margin-bottom: 40px;
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .sidebar .nav-link {
            color: #fff;
            font-size: 1.15rem;
            padding: 15px;
            display: flex;
            align-items: center;
            gap: 15px;
            border-radius: 10px;
            transition: all 0.3s ease;
            position: relative;
        }

        .sidebar .nav-link:hover {
            background: rgba(255, 255, 255, 0.25);
            transform: translateX(5px);
        }

        .sidebar .nav-link i {
            font-size: 1.3rem;
        }

        .sidebar.collapsed .nav-link span, .sidebar.collapsed .sub-menu {
            display: none;
        }

        .sidebar .sub-menu {
            margin-left: 30px;
            display: none;
        }

        .sidebar .nav-item.active .sub-menu {
            display: block;
        }

        .sidebar .dropdown-menu {
            background: #fff;
            border: none;
            border-radius: 10px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
            min-width: 260px;
            position: absolute;
            left: 310px;
            top: 0;
        }

        .dark-mode .dropdown-menu {
            background: #1f2937;
            color: #f8f9fa;
        }

        .custom-theme .dropdown-menu {
            background: #fff1f2;
            color: #4b2e2e;
        }

        .dropdown-item {
            font-size: 1rem;
            padding: 12px 20px;
            transition: all 0.3s ease;
        }

        .dropdown-item:hover {
            background: #1e3a8a;
            color: #fff;
        }

        .profile-info {
            padding: 20px;
            border-bottom: 1px solid rgba(255, 255, 255, 0.2);
            font-size: 0.95rem;
        }

        /* Main Content */
        .main-content {
            margin-left: 300px;
            padding: 50px;
            transition: margin-left 0.3s ease;
        }

        .main-content.expanded {
            margin-left: 90px;
        }

        .dashboard-title {
            font-size: 3rem;
            font-weight: 700;
            text-align: center;
            margin-bottom: 60px;
            background: linear-gradient(90deg, #1e3a8a, #3b82f6);
            -webkit-background-clip: text;
            -webkit-text-fill-color: transparent;
            animation: fadeInDown 1s ease;
        }

        .card {
            background: #fff;
            border-radius: 20px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.15);
            transition: all 0.3s ease;
            margin-bottom: 40px;
            overflow: hidden;
            opacity: 0;
        }

        .card.loaded {
            opacity: 1;
            animation: fadeInUp 0.8s ease-in-out;
        }

        .dark-mode .card {
            background: #1f2937;
        }

        .custom-theme .card {
            background: #fff1f2;
        }

        .card:hover {
            transform: translateY(-10px);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.25);
        }

        .card-header {
            background: linear-gradient(90deg, #1e3a8a, #3b82f6);
            color: #fff;
            padding: 25px;
            font-size: 1.6rem;
            font-weight: 600;
            display: flex;
            align-items: center;
            gap: 15px;
            border-bottom: 2px solid rgba(255, 255, 255, 0.2);
        }

        .dark-mode .card-header {
            background: linear-gradient(90deg, #111827, #374151);
        }

        .custom-theme .card-header {
            background: linear-gradient(90deg, #ff6b6b, #feb2b2);
        }

        .card-body {
            padding: 35px;
            background: #f9fafb;
        }

        .dark-mode .card-body {
            background: #374151;
        }

        .custom-theme .card-body {
            background: #fef9e7;
        }

        .form-group {
            margin-bottom: 25px;
        }

        .form-group label {
            font-weight: 500;
            font-size: 1.1rem;
            margin-bottom: 8px;
            display: block;
        }

        .form-group input {
            width: 100%;
            padding: 12px 20px;
            border-radius: 10px;
            border: 1px solid #ced4da;
            font-size: 1rem;
            transition: all 0.3s ease;
        }

        .form-group input:focus {
            border-color: #1e3a8a;
            box-shadow: 0 0 8px rgba(30, 58, 138, 0.3);
            outline: none;
        }

        .dark-mode .form-group input {
            background: #374151;
            border-color: #4b5563;
            color: #f8f9fa;
        }

        .dark-mode .form-group input:focus {
            border-color: #60a5fa;
            box-shadow: 0 0 8px rgba(96, 165, 250, 0.3);
        }

        .btn-custom {
            display: flex;
            align-items: center;
            gap: 12px;
            width: 100%;
            padding: 15px 30px;
            margin-bottom: 20px;
            font-size: 1.2rem;
            font-weight: 500;
            border-radius: 10px;
            color: #fff;
            text-decoration: none;
            transition: all 0.3s ease;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            border: none;
        }

        .btn-custom:hover {
            transform: scale(1.05);
            filter: brightness(1.2);
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.2);
        }

        .btn-primary {
            background: #1e3a8a;
        }

        .btn-secondary {
            background: #6b7280;
        }

        .search-bar {
            position: relative;
            margin-bottom: 40px;
        }

        .search-bar input {
            width: 100%;
            padding: 15px 25px;
            border-radius: 30px;
            border: none;
            font-size: 1.2rem;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
            transition: all 0.3s ease;
        }

        .search-bar input:focus {
            outline: none;
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
        }

        .search-bar .suggestions {
            position: absolute;
            top: 100%;
            left: 0;
            width: 100%;
            background: #fff;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.2);
            z-index: 1000;
            display: none;
            max-height: 300px;
            overflow-y: auto;
        }

        .dark-mode .search-bar .suggestions {
            background: #1f2937;
            color: #f8f9fa;
        }

        .search-bar .suggestions a {
            display: block;
            padding: 12px 25px;
            color: #343a40;
            text-decoration: none;
            transition: all 0.3s ease;
        }

        .dark-mode .search-bar .suggestions a {
            color: #f8f9fa;
        }

        .search-bar .suggestions a:hover {
            background: #1e3a8a;
            color: #fff;
        }

        .notification-panel {
            position: fixed;
            top: 20px;
            right: 20px;
            width: 350px;
            max-height: 500px;
            overflow-y: auto;
            z-index: 2000;
        }

        .notification {
            background: #fff;
            padding: 20px;
            margin-bottom: 15px;
            border-radius: 15px;
            box-shadow: 0 6px 15px rgba(0, 0, 0, 0.1);
            animation: slideInRight 0.5s ease;
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .dark-mode .notification {
            background: #1f2937;
            color: #f8f9fa;
        }

        .notification i {
            font-size: 1.5rem;
            color: #1e3a8a;
        }

        .dark-mode .notification i {
            color: #60a5fa;
        }

        .error-message {
            color: #dc2626;
            font-size: 0.9rem;
            margin-top: 5px;
            display: none;
        }

        .alert {
            margin-bottom: 20px;
        }

        @keyframes fadeInUp {
            from {
                opacity: 0;
                transform: translateY(20px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        @keyframes slideInRight {
            from {
                opacity: 0;
                transform: translateX(100%);
            }
            to {
                opacity: 1;
                transform: translateX(0);
            }
        }

        @media (max-width: 768px) {
            .sidebar {
                width: 90px;
            }

            .main-content {
                margin-left: 90px;
                padding: 30px;
            }

            .sidebar .nav-link span, .sidebar .sub-menu {
                display: none;
            }

            .dashboard-title {
                font-size: 2.2rem;
            }

            .btn-custom {
                font-size: 1rem;
                padding: 12px 20px;
            }

            .notification-panel {
                width: 100%;
                right: 0;
                padding: 0 15px;
            }
        }
    </style>
</head>
<body>
<%
    Account account = (Account) session.getAttribute("account");
    if (account == null || account.getRoleId() != 1) { // Chỉ Admin (Role_ID = 1) được thêm staff
        response.sendRedirect("HomePage");
        return;
    }
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
        <h1 class="dashboard-title">Add New Staff</h1>

        <!-- Search Bar -->
        <div class="search-bar">
            <input type="text" id="searchInput" placeholder="Search anything...">
            <div class="suggestions" id="suggestions"></div>
        </div>

        <div class="row justify-content-center">
            <div class="col-lg-6">
                <div class="card">
                    <div class="card-header">
                        <i class="fas fa-user-tie"></i> Add Staff Form
                    </div>
                    <div class="card-body">
                        <!-- Hiển thị thông báo lỗi từ Servlet -->
                        <%
                            String error = (String) request.getAttribute("error");
                            String success = (String) request.getAttribute("success");
                            if (error != null) {
                        %>
                        <div class="alert alert-danger" role="alert">
                            <%= error %>
                        </div>
                        <%
                        } else if (success != null) {
                        %>
                        <div class="alert alert-success" role="alert">
                            <%= success %>
                        </div>
                        <%
                            }
                        %>

                        <form action="AddStaff" method="post" onsubmit="return validateForm()">
                            <input type="hidden" name="role_id" value="2"> <!-- Role_ID = 2 cho Staff -->

                            <div class="form-group">
                                <label for="fullName">Full Name</label>
                                <input type="text" id="fullName" name="fullName" required>
                                <div id="fullNameError" class="error-message">Full Name is required.</div>
                            </div>

                            <div class="form-group">
                                <label for="email">Email</label>
                                <input type="email" id="email" name="email" required>
                                <div id="emailError" class="error-message">Please enter a valid email.</div>
                            </div>

                            <div class="form-group">
                                <label for="username">Username</label>
                                <input type="text" id="username" name="username" required>
                                <div id="usernameError" class="error-message">Username is required.</div>
                            </div>

                            <div class="form-group">
                                <label for="password">Password</label>
                                <input type="password" id="password" name="password" required>
                                <div id="passwordError" class="error-message">Password must be at least 6 characters.
                                </div>
                            </div>

                            <button type="submit" class="btn-custom btn-primary"><i class="fas fa-plus"></i> Add Staff
                            </button>
                            <a href="HomePage" class="btn-custom btn-secondary"><i
                                    class="fas fa-arrow-left"></i> Back to Dashboard</a>
                        </form>
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
    // Card Animation
    window.addEventListener('load', () => {
        document.querySelectorAll('.card').forEach(card => card.classList.add('loaded'));
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

    // Search Bar
    const searchInput = document.getElementById('searchInput');
    const suggestions = document.getElementById('suggestions');
    const searchItems = [
        'Add Book', 'Manage Books', 'Delete Book',
        'Ban Member', 'Unban Member', 'Update Member',
        'View Borrowing History',
        'Manage Fines', 'View Payments',
        'Add Staff', 'Manage Staff', 'Delete Staff', 'Staff Activity',
        'System Settings'
    ];
    searchInput.addEventListener('input', () => {
        const query = searchInput.value.toLowerCase();
        suggestions.innerHTML = '';
        if (query) {
            const filtered = searchItems.filter(item => item.toLowerCase().includes(query));
            filtered.forEach(item => {
                const a = document.createElement('a');
                a.href = '#';
                a.textContent = item;
                suggestions.appendChild(a);
            });
            suggestions.style.display = 'block';
        } else {
            suggestions.style.display = 'none';
        }
    });

    // Real-time Notifications
    const notificationPanel = document.getElementById('notificationPanel');
    const addNotification = (message, icon) => {
        const notification = document.createElement('div');
        notification.className = 'notification';
        notification.innerHTML = `<i class="${icon}"></i> ${message}`;
        notificationPanel.insertBefore(notification, notificationPanel.firstChild);
        setTimeout(() => notification.remove(), 6000);
    };

    setInterval(() => {
        const messages = [
            {text: 'New book added to the library!', icon: 'fas fa-book'},
            {text: 'Member account updated.', icon: 'fas fa-user-edit'},
            {text: 'Fine payment processed.', icon: 'fas fa-money-bill'},
            {text: 'New staff added.', icon: 'fas fa-user-tie'},
            {text: 'System backup completed.', icon: 'fas fa-database'}
        ];
        const random = messages[Math.floor(Math.random() * messages.length)];
        addNotification(random.text, random.icon);
    }, 8000);

    // Form Validation
    function validateForm() {
        let isValid = true;

        // Reset error messages
        document.querySelectorAll('.error-message').forEach(error => error.style.display = 'none');

        // Full Name
        const fullName = document.getElementById('fullName').value.trim();
        if (!fullName) {
            document.getElementById('fullNameError').style.display = 'block';
            isValid = false;
        }

        // Email
        const email = document.getElementById('email').value.trim();
        const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        if (!emailRegex.test(email)) {
            document.getElementById('emailError').style.display = 'block';
            isValid = false;
        }

        // Username
        const username = document.getElementById('username').value.trim();
        if (!username) {
            document.getElementById('usernameError').style.display = 'block';
            isValid = false;
        }

        // Password
        const password = document.getElementById('password').value;
        if (password.length < 6) {
            document.getElementById('passwordError').style.display = 'block';
            isValid = false;
        }

        return isValid;
    }
</script>
</body>
</html>