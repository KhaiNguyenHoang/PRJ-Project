<%@ page import="model.entity.Members" %>
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
    <title>Tài khoản</title>
    <!-- Fonts style -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,700&display=swap" rel="stylesheet"/>
    <!-- Bootstrap CSS -->
    <link href="HomeHTML/HomeMemberHTML/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="HomeHTML/HomeMemberHTML/css/style.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/responsive.css" rel="stylesheet"/>
    <!-- Font Awesome for icons -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    <style>
        /* Account Section */
        .account-section {
            padding: 60px 0;
        }

        .account-card {
            border-radius: 20px;
            box-shadow: 0 20px 50px rgba(0, 0, 0, 0.1);
            background-color: #ffffff;
            transition: transform 0.3s ease, box-shadow 0.3s ease;
        }

        .account-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 30px 60px rgba(0, 0, 0, 0.15);
        }

        .card-header {
            background: linear-gradient(90deg, #00bcd4, #0097a7);
            color: white;
            font-size: 1.8rem;
            text-align: center;
            padding: 25px;
            border-radius: 20px 20px 0 0;
            font-weight: 600;
            letter-spacing: 1px;
        }

        .card-body {
            padding: 40px;
            background-color: #f9fafc;
            border-radius: 0 0 20px 20px;
        }

        /* Form Styling */
        .form-label {
            font-weight: 600;
            color: #333;
            margin-bottom: 10px;
        }

        .form-control {
            border-radius: 12px;
            border: 2px solid #ddd;
            padding: 12px 15px;
            font-size: 1rem;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }

        .form-control:focus {
            border-color: #00bcd4;
            box-shadow: 0 0 10px rgba(0, 188, 212, 0.2);
        }

        input[readonly] {
            background-color: #f1f3f5;
            cursor: not-allowed;
            color: #6c757d;
        }

        /* Button Styling */
        .btn-custom {
            background-color: #00bcd4;
            color: white;
            padding: 12px;
            border-radius: 12px;
            font-size: 1.2rem;
            font-weight: 600;
            border: none;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 10px rgba(0, 188, 212, 0.2);
        }

        .btn-custom:hover {
            background-color: #00acc1;
            transform: translateY(-3px);
            box-shadow: 0 6px 15px rgba(0, 188, 212, 0.3);
        }

        .btn-delete {
            background-color: #e74c3c;
            color: white;
            padding: 12px;
            border-radius: 12px;
            font-size: 1.2rem;
            font-weight: 600;
            border: none;
            transition: background-color 0.3s ease, transform 0.2s ease;
            box-shadow: 0 4px 10px rgba(231, 76, 60, 0.2);
        }

        .btn-delete:hover {
            background-color: #c0392b;
            transform: translateY(-3px);
            box-shadow: 0 6px 15px rgba(231, 76, 60, 0.3);
        }

        /* Collapse Animation */
        .collapse:not(.show) {
            display: none;
        }

        .collapsing {
            transition: height 0.3s ease;
        }

        /* Alert Styling */
        .alert {
            border-radius: 10px;
            font-size: 1rem;
            margin-top: 20px;
        }

        /* Toast Notification */
        .toast {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 50;
            background-color: #28a745;
            color: white;
            padding: 15px;
            border-radius: 5px;
            display: none;
        }
    </style>
</head>
<body class="bg-gradient-to-r from-red-500 via-purple-600 to-pink-600 text-white">

<!-- Header Section -->
<header class="header_section">
    <div class="container-fluid">
        <nav class="navbar navbar-expand-lg custom_nav-container">
            <a class="navbar-brand" href="index.jsp">
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

<!-- Account Management Section -->
<div class="container account-section">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="card account-card">
                <div class="card-header">
                    <i class="fas fa-user-cog me-2"></i> Account Management
                </div>
                <div class="card-body">
                    <%
                        Members loggedInMember = (Members) session.getAttribute("user");
                        if (loggedInMember != null) {
                    %>

                    <!-- Edit Info Form -->
                    <form action="Account" method="post" id="updateForm">
                        <input type="hidden" name="action" value="changeInfo">
                        <div class="mb-4">
                            <label for="fullName" class="form-label">Full Name</label>
                            <input type="text" class="form-control" id="fullName" name="fullName"
                                   value="<%= loggedInMember.getFullName() %>" required/>
                        </div>
                        <div class="mb-4">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="<%= loggedInMember.getEmail() %>" readonly/>
                            <small class="form-text text-muted">Email cannot be changed here. Contact support for
                                assistance.</small>
                        </div>
                        <div class="mb-4">
                            <label for="phone" class="form-label">Phone</label>
                            <input type="text" class="form-control" id="phone" name="phone"
                                   value="<%= loggedInMember.getPhone() %>" required/>
                        </div>
                        <div class="mb-4">
                            <label for="address" class="form-label">Address</label>
                            <input type="text" class="form-control" id="address" name="address"
                                   value="<%= loggedInMember.getAddress() %>" required/>
                        </div>
                        <%
                            // Handle error messages
                            String error = (String) request.getAttribute("error");
                            if (error != null) {
                        %>
                        <div class="alert alert-danger">
                            <% if ("empty_field".equals(error)) { %>All fields must be filled out.<% } %>
                            <% if ("invalid_email".equals(error)) { %>Please provide a valid email address.<% } %>
                            <% if ("invalid_phone".equals(error)) { %>Please provide a valid phone number (10
                            digits).<% } %>
                            <% if ("server_error".equals(error)) { %>An error occurred while updating your information.
                            Please try again later.<% } %>
                        </div>
                        <% } %>

                        <%
                            // Handle success message
                            String success = (String) request.getAttribute("success");
                            if ("update_success".equals(success)) {
                        %>
                        <div class="alert alert-success">Your information has been updated successfully!</div>
                        <% } %>
                        <button type="submit" class="btn btn-custom w-100">Save Changes</button>
                    </form>

                    <!-- Change Password Section -->
                    <div class="mt-4">
                        <button class="btn btn-custom w-100" type="button" data-bs-toggle="collapse"
                                data-bs-target="#passwordForm">
                            <i class="fas fa-key me-2"></i> Change Password
                        </button>
                        <div class="collapse mt-3" id="passwordForm">
                            <form action="ChangePassword" method="post" id="passwordChangeForm">
                                <div class="mb-3">
                                    <label for="oldPassword" class="form-label">Current Password</label>
                                    <input type="password" class="form-control" id="oldPassword" name="oldPassword"
                                           placeholder="Enter current password" required/>
                                </div>
                                <div class="mb-3">
                                    <label for="newPassword" class="form-label">New Password</label>
                                    <input type="password" class="form-control" id="newPassword" name="newPassword"
                                           placeholder="Enter new password" required/>
                                </div>
                                <div class="mb-3">
                                    <label for="confirmNewPassword" class="form-label">Confirm New Password</label>
                                    <input type="password" class="form-control" id="confirmNewPassword"
                                           name="confirmNewPassword" placeholder="Confirm new password" required/>
                                </div>
                                <button type="submit" class="btn btn-custom w-100">Update Password</button>
                            </form>
                        </div>
                    </div>

                    <!-- Deactivate Account Section -->
                    <div class="mt-4">
                        <button class="btn btn-delete w-100" type="button" data-bs-toggle="collapse"
                                data-bs-target="#deactivateForm">
                            <i class="fas fa-user-slash me-2"></i> Deactivate Account
                        </button>
                        <div class="collapse mt-3" id="deactivateForm">
                            <form action="DeactivateAccount" method="post" id="deactivateAccountForm">
                                <div class="mb-3">
                                    <label for="confirmPassword" class="form-label">Confirm Password</label>
                                    <input type="password" class="form-control" id="confirmPassword"
                                           name="confirmPassword" placeholder="Confirm your password" required/>
                                </div>
                                <button type="submit" class="btn btn-delete w-100">Deactivate Account</button>
                            </form>
                        </div>
                    </div>

                    <%
                    } else {
                    %>
                    <div class="alert alert-danger text-center" role="alert">
                        You are not logged in. Please <a href="login.jsp" class="alert-link">login</a> to manage your
                        account.
                    </div>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Toast Notification -->
<div class="toast" id="successToast">
    <i class="fas fa-check-circle"></i> Cập nhật thành công!
</div>

<!-- Footer Section -->
<section class="container-fluid footer_section">
    <p>© 2024 Group 2 All Rights Reserved | Design by Group 2</p>
</section>

<script>
    // Client-side validation and feedback
    document.getElementById('updateForm')?.addEventListener('submit', function (e) {
        const fullName = document.getElementById('fullName').value.trim();
        const phone = document.getElementById('phone').value.trim();
        const address = document.getElementById('address').value.trim();
        if (!fullName || !phone || !address) {
            e.preventDefault();
            alert('Please fill in all required fields!');
        }
    });

    document.getElementById('passwordChangeForm')?.addEventListener('submit', function (e) {
        const oldPassword = document.getElementById('oldPassword').value.trim();
        const newPassword = document.getElementById('newPassword').value.trim();
        const confirmNewPassword = document.getElementById('confirmNewPassword').value.trim();
        if (!oldPassword || !newPassword || !confirmNewPassword) {
            e.preventDefault();
            alert('Please fill in all password fields!');
        } else if (newPassword !== confirmNewPassword) {
            e.preventDefault();
            alert('New passwords do not match!');
        } else if (newPassword.length < 6) {
            e.preventDefault();
            alert('New password must be at least 6 characters long!');
        }
    });

    document.getElementById('deactivateAccountForm')?.addEventListener('submit', function (e) {
        const confirmPassword = document.getElementById('confirmPassword').value.trim();
        if (!confirmPassword) {
            e.preventDefault();
            alert('Please confirm your password!');
        } else if (!confirm('Are you sure you want to deactivate your account? This action cannot be undone.')) {
            e.preventDefault();
        }
    });

    // Show toast notification on success
    const success = '<%= request.getAttribute("success") %>';
    if (success === 'update_success') {
        const toast = document.getElementById('successToast');
        toast.style.display = 'block';
        setTimeout(() => {
            toast.style.display = 'none';
        }, 3000);
    }
</script>
</body>
</html>