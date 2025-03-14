<%@ page import="model.entity.Members" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    Members currentMember = (Members) request.getSession().getAttribute("user");
    if (currentMember == null) {
        response.sendRedirect("/Auth/SignIn-SignUp.jsp");
        return;
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>Tài khoản</title>
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,700&display=swap" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="HomeHTML/HomeMemberHTML/css/style.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/responsive.css" rel="stylesheet"/>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        /* Add your custom styles here */
        .account-section {
            padding: 60px 0;
        }

        .account-card {
            border-radius: 20px;
            box-shadow: 0 20px 50px rgba(0, 0, 0, 0.1);
            background-color: #ffffff;
        }

        .card-header {
            background: linear-gradient(90deg, #00bcd4, #0097a7);
            color: white;
            text-align: center;
            padding: 25px;
            border-radius: 20px 20px 0 0;
        }

        .card-body {
            padding: 40px;
            background-color: #f9fafc;
            border-radius: 0 0 20px 20px;
        }

        .btn-custom {
            background-color: #00bcd4;
            color: white;
            padding: 12px;
            border-radius: 12px;
        }

        .btn-delete {
            background-color: #e74c3c;
            color: white;
            padding: 12px;
            border-radius: 12px;
        }

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
    <!-- Header content -->
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
                    <!-- Edit Info Form -->
                    <form action="Account" method="post" id="updateForm">
                        <input type="hidden" name="action" value="changeInfo">
                        <div class="mb-4">
                            <label for="fullName" class="form-label">Full Name</label>
                            <input type="text" class="form-control" id="fullName" name="fullName"
                                   value="<%= currentMember.getFullName() %>" required/>
                        </div>
                        <div class="mb-4">
                            <label for="email" class="form-label">Email</label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="<%= currentMember.getEmail() %>" readonly/>
                            <small class="form-text text-muted">Email cannot be changed here. Contact support for
                                assistance.</small>
                        </div>
                        <div class="mb-4">
                            <label for="phone" class="form-label">Phone</label>
                            <input type="text" class="form-control" id="phone" name="phone"
                                   value="<%= currentMember.getPhone() %>" required/>
                        </div>
                        <div class="mb-4">
                            <label for="address" class="form-label">Address</label>
                            <input type="text" class="form-control" id="address" name="address"
                                   value="<%= currentMember.getAddress() %>" required/>
                        </div>
                        <% if (request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger">
                            <% String error = (String) request.getAttribute("error"); %>
                            <% if ("empty_field".equals(error)) { %>All fields must be filled out.<% } %>
                            <% if ("invalid_email".equals(error)) { %>Please provide a valid email address.<% } %>
                            <% if ("invalid_phone".equals(error)) { %>Please provide a valid phone number (10
                            digits).<% } %>
                            <% if ("update_failed".equals(error)) { %>Failed to update information. Please try
                            again.<% } %>
                        </div>
                        <% } %>
                        <% if ("update_success".equals(request.getAttribute("success"))) { %>
                        <div class="alert alert-success">Your account information has been updated successfully!</div>
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
                            <form action="Account" method="post" id="passwordChangeForm">
                                <input type="hidden" name="action" value="changePassword">
                                <div class="mb-3">
                                    <label for="currentPassword" class="form-label">Current Password</label>
                                    <input type="password" class="form-control" id="currentPassword"
                                           name="currentPassword" placeholder="Enter current password" required/>
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
                                <% if (request.getAttribute("error-password") != null) { %>
                                <div class="alert alert-danger">
                                    <% String passwordError = (String) request.getAttribute("error-password"); %>
                                    <% if ("empty_password_field".equals(passwordError)) { %>All password fields must be
                                    filled out.<% } %>
                                    <% if ("password_mismatch".equals(passwordError)) { %>New passwords do not
                                    match.<% } %>
                                    <% if ("incorrect_current_password".equals(passwordError)) { %>Incorrect current
                                    password.<% } %>
                                    <% if ("password_change_failed".equals(passwordError)) { %>Failed to change
                                    password. Please try again.<% } %>
                                </div>
                                <% } %>
                                <% if ("password_changed".equals(request.getAttribute("success-password"))) { %>
                                <div class="alert alert-success">Your password has been changed successfully!</div>
                                <% } %>
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

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Form validation and feedback
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
        const currentPassword = document.getElementById('currentPassword').value.trim();
        const newPassword = document.getElementById('newPassword').value.trim();
        const confirmNewPassword = document.getElementById('confirmNewPassword').value.trim();
        if (!currentPassword || !newPassword || !confirmNewPassword) {
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
    const passwordSuccess = '<%= request.getAttribute("success-password") %>';
    if (success === 'update_success' || passwordSuccess === 'password_changed') {
        const toast = document.getElementById('successToast');
        toast.style.display = 'block';
        setTimeout(() => {
            toast.style.display = 'none';
        }, 3000);
    }
</script>
</body>
</html>