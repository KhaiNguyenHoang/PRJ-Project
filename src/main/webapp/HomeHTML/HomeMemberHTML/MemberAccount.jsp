<%@ page import="model.Members" %>
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
    <!-- Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@400;600;700&display=swap" rel="stylesheet"/>
    <!-- Original Header/Footer CSS -->
    <link href="HomeHTML/HomeMemberHTML/css/bootstrap.css" rel="stylesheet" type="text/css"/>
    <link href="HomeHTML/HomeMemberHTML/css/style.css" rel="stylesheet"/>
    <link href="HomeHTML/HomeMemberHTML/css/responsive.css" rel="stylesheet"/>
    <!-- Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- Animate.css -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <!-- Font Awesome -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css"/>
    <!-- Custom CSS -->
    <style>
        body {
            font-family: 'Poppins', sans-serif;
            background: #f6f8fa;
            min-height: 100vh;
            overflow-x: hidden;
        }

        .account-section {
            padding: 70px 0;
        }

        .account-card {
            border: none;
            background: #fff;
            border-radius: 20px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.06);
            transition: transform 0.4s ease, box-shadow 0.4s ease;
            position: relative;
            overflow: hidden;
        }

        .account-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 12px 35px rgba(0, 0, 0, 0.1);
        }

        .card-header {
            background: #1e2a44;
            color: #fff;
            text-align: center;
            padding: 25px;
            font-size: 1.8rem;
            font-weight: 700;
            border-radius: 20px 20px 0 0;
            position: relative;
            z-index: 1;
        }

        .card-header::after {
            content: '';
            position: absolute;
            bottom: -10px;
            left: 50%;
            transform: translateX(-50%);
            width: 50px;
            height: 2px;
            background: #007bff;
        }

        .card-body {
            padding: 35px;
            background: #fafbfc;
        }

        .form-label {
            font-weight: 600;
            color: #1e2a44;
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .form-control {
            border-radius: 10px;
            border: 1px solid #dfe4ea;
            padding: 12px 15px;
            font-size: 1rem;
            transition: all 0.3s ease;
            background: #fff;
        }

        .form-control:focus {
            border-color: #007bff;
            box-shadow: 0 0 10px rgba(0, 123, 255, 0.15);
            outline: none;
        }

        .form-control:disabled, .form-control[readonly] {
            background: #e9ecef;
            opacity: 0.8;
        }

        .btn-custom {
            background: #007bff;
            color: #fff;
            padding: 12px 25px;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            border: none;
            position: relative;
            overflow: hidden;
        }

        .btn-custom:hover {
            background: #0056b3;
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(0, 86, 179, 0.3);
        }

        .btn-delete {
            background: #dc3545;
            color: #fff;
            padding: 12px 25px;
            border-radius: 10px;
            font-weight: 600;
            transition: all 0.3s ease;
            border: none;
            position: relative;
            overflow: hidden;
        }

        .btn-delete:hover {
            background: #c82333;
            transform: translateY(-3px);
            box-shadow: 0 5px 15px rgba(200, 35, 51, 0.3);
        }

        .btn-custom::after, .btn-delete::after {
            content: '';
            position: absolute;
            top: 50%;
            left: 50%;
            width: 0;
            height: 0;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 50%;
            transform: translate(-50%, -50%);
            transition: width 0.6s ease, height 0.6s ease;
        }

        .btn-custom:hover::after, .btn-delete:hover::after {
            width: 200%;
            height: 200%;
        }

        .alert {
            border-radius: 10px;
            padding: 15px 20px;
            margin-bottom: 25px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.05);
            border: none;
            font-size: 0.95rem;
        }

        .alert-success {
            background: #e6f4ea;
            color: #1c7430;
        }

        .alert-danger {
            background: #fce8e6;
            color: #842029;
        }

        .collapse-section {
            margin-top: 25px;
            padding: 20px;
            background: #fff;
            border-radius: 12px;
            box-shadow: 0 4px 15px rgba(0, 0, 0, 0.03);
            transition: all 0.3s ease;
        }

        .collapse-section:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.06);
        }

        .collapse-section .btn {
            width: 100%;
            text-align: left;
            display: flex;
            align-items: center;
            justify-content: space-between;
        }

        .collapse-section .btn::after {
            content: '\f078'; /* Font Awesome chevron-down */
            font-family: 'Font Awesome 5 Free';
            font-weight: 900;
            transition: transform 0.3s ease;
        }

        .collapse-section .btn[aria-expanded="true"]::after {
            transform: rotate(180deg);
        }

        .toast {
            position: fixed;
            top: 25px;
            right: 25px;
            z-index: 1050;
            background: #28a745;
            color: #fff;
            padding: 15px 25px;
            border-radius: 12px;
            box-shadow: 0 6px 20px rgba(0, 0, 0, 0.15);
            display: none;
            opacity: 0;
            transition: opacity 0.4s ease, transform 0.4s ease;
            transform: translateY(-20px);
        }

        .toast.show {
            display: flex;
            align-items: center;
            opacity: 1;
            transform: translateY(0);
        }

        .toast i {
            margin-right: 10px;
        }
    </style>
</head>
<body>
<!-- Header (Unchanged) -->
<header class="header_section">
    <div class="container-fluid">
        <nav class="navbar navbar-expand-lg custom_nav-container ">
            <a class="navbar-brand" href="HomePage">
                <span>Library</span>
            </a>
            <button aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"
                    class="navbar-toggler" data-target="#navbarSupportedContent" data-toggle="collapse" type="button">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <div class="d-flex ml-auto flex-column flex-lg-row align-items-center">
                    <ul class="navbar-nav">
                        <li class="nav-item active">
                            <a class="nav-link" href="HomePage">Home <span class="sr-only">(current)</span></a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="HomePage#about_section"> About </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="HomePage#how_section layout_padding"> How </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="Borrowing"> Borrowing </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="MemberAccount"> Account </a>
                        </li>
                        <li class="nav-item">
                            <a class="nav-link" href="logoutAuth"> Logout</a>
                        </li>
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

<!-- Account Management Section -->
<div class="container account-section">
    <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
            <div class="account-card animate__animated animate__fadeInUpBig">
                <div class="card-header">
                    <i class="fas fa-user-cog me-2"></i> Account Management
                </div>
                <div class="card-body">
                    <!-- Edit Info Form -->
                    <form action="Account" method="post" id="updateForm" class="needs-validation" novalidate>
                        <input type="hidden" name="action" value="changeInfo">
                        <div class="mb-4 animate__animated animate__fadeIn" data-animate-delay="0.1s">
                            <label for="fullName" class="form-label"><i class="fas fa-user me-2"></i> Full Name</label>
                            <input type="text" class="form-control" id="fullName" name="fullName"
                                   value="<%= currentMember.getFullName() %>" required/>
                            <div class="invalid-feedback">Please enter your full name.</div>
                        </div>
                        <div class="mb-4 animate__animated animate__fadeIn" data-animate-delay="0.2s">
                            <label for="email" class="form-label"><i class="fas fa-envelope me-2"></i> Email</label>
                            <input type="email" class="form-control" id="email" name="email"
                                   value="<%= currentMember.getEmail() %>" readonly/>
                            <small class="form-text text-muted">Contact support to change your email.</small>
                        </div>
                        <div class="mb-4 animate__animated animate__fadeIn" data-animate-delay="0.3s">
                            <label for="phone" class="form-label"><i class="fas fa-phone me-2"></i> Phone</label>
                            <input type="text" class="form-control" id="phone" name="phone"
                                   value="<%= currentMember.getPhone() %>" required/>
                            <div class="invalid-feedback">Please enter a valid phone number.</div>
                        </div>
                        <div class="mb-4 animate__animated animate__fadeIn" data-animate-delay="0.4s">
                            <label for="address" class="form-label"><i class="fas fa-map-marker-alt me-2"></i>
                                Address</label>
                            <input type="text" class="form-control" id="address" name="address"
                                   value="<%= currentMember.getAddress() %>" required/>
                            <div class="invalid-feedback">Please enter your address.</div>
                        </div>
                        <% if (request.getAttribute("error") != null) { %>
                        <div class="alert alert-danger animate__animated animate__shakeX">
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
                        <div class="alert alert-success animate__animated animate__bounceIn">Your account information
                            has been updated successfully!
                        </div>
                        <% } %>
                        <button type="submit" class="btn btn-custom w-100 animate__animated animate__fadeIn"
                                data-animate-delay="0.5s">
                            <i class="fas fa-save me-2"></i> Save Changes
                        </button>
                    </form>

                    <!-- Change Password Section -->
                    <div class="collapse-section mt-4 animate__animated animate__fadeIn" data-animate-delay="0.6s">
                        <button class="btn btn-custom" type="button" data-bs-toggle="collapse"
                                data-bs-target="#passwordForm" aria-expanded="false" aria-controls="passwordForm">
                            <span><i class="fas fa-key me-2"></i> Change Password</span>
                        </button>
                        <div class="collapse mt-3" id="passwordForm">
                            <form action="Account" method="post" id="passwordChangeForm" class="needs-validation"
                                  novalidate>
                                <input type="hidden" name="action" value="changePassword">
                                <div class="mb-3">
                                    <label for="currentPassword" class="form-label"><i class="fas fa-lock me-2"></i>
                                        Current Password</label>
                                    <input type="password" class="form-control" id="currentPassword"
                                           name="currentPassword"
                                           placeholder="Enter current password" required/>
                                    <div class="invalid-feedback">Please enter your current password.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="newPassword" class="form-label"><i class="fas fa-lock me-2"></i> New
                                        Password</label>
                                    <input type="password" class="form-control" id="newPassword" name="newPassword"
                                           placeholder="Enter new password" required/>
                                    <div class="invalid-feedback">Please enter a new password.</div>
                                </div>
                                <div class="mb-3">
                                    <label for="confirmNewPassword" class="form-label"><i class="fas fa-lock me-2"></i>
                                        Confirm New Password</label>
                                    <input type="password" class="form-control" id="confirmNewPassword"
                                           name="confirmNewPassword"
                                           placeholder="Confirm new password" required/>
                                    <div class="invalid-feedback">Please confirm your new password.</div>
                                </div>
                                <% if (request.getAttribute("error-password") != null) { %>
                                <div class="alert alert-danger animate__animated animate__shakeX">
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
                                <% if ("password_changed_successfully".equals(request.getAttribute("success-password"))) { %>
                                <div class="alert alert-success animate__animated animate__bounceIn">Your password has
                                    been changed successfully!
                                </div>
                                <% } %>
                                <button type="submit" class="btn btn-custom w-100">
                                    <i class="fas fa-sync-alt me-2"></i> Update Password
                                </button>
                            </form>
                        </div>
                    </div>

                    <!-- Deactivate Account Section -->
                    <div class="collapse-section mt-4 animate__animated animate__fadeIn" data-animate-delay="0.7s">
                        <button class="btn btn-delete" type="button" data-bs-toggle="collapse"
                                data-bs-target="#deactivateForm" aria-expanded="false" aria-controls="deactivateForm">
                            <span><i class="fas fa-user-slash me-2"></i> Deactivate Account</span>
                        </button>
                        <div class="collapse mt-3" id="deactivateForm">
                            <form action="Account" method="post" id="deactivateAccountForm" class="needs-validation"
                                  novalidate>
                                <input type="hidden" name="action" value="deactiveAccount">
                                <div class="mb-3">
                                    <label for="confirmPassword" class="form-label"><i class="fas fa-lock me-2"></i>
                                        Confirm Password</label>
                                    <input type="password" class="form-control" id="confirmPassword"
                                           name="confirmPassword"
                                           placeholder="Confirm your password" required/>
                                    <div class="invalid-feedback">Please confirm your password.</div>
                                </div>
                                <button type="submit" class="btn btn-delete w-100">
                                    <i class="fas fa-trash-alt me-2"></i> Deactivate Account
                                </button>
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

<!-- Footer (Unchanged) -->
<section class="container-fluid footer_section">
    <p>© 2024 Group 2 All Rights Reserved | Design by Group 2</p>
</section>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
<script>
    // Form validation with Bootstrap
    document.querySelectorAll('.needs-validation').forEach(form => {
        form.addEventListener('submit', function (e) {
            if (!form.checkValidity()) {
                e.preventDefault();
                e.stopPropagation();
            }
            form.classList.add('was-validated');
        });
    });

    // Enhanced password change form validation
    document.getElementById('passwordChangeForm')?.addEventListener('submit', function (e) {
        const newPassword = document.getElementById('newPassword').value.trim();
        const confirmNewPassword = document.getElementById('confirmNewPassword').value.trim();
        if (newPassword !== confirmNewPassword) {
            e.preventDefault();
            alert('New passwords do not match!');
        } else if (newPassword.length < 8) {
            e.preventDefault();
            alert('New password must be at least 8 characters long!');
        }
    });

    // Deactivate account confirmation
    document.getElementById('deactivateAccountForm')?.addEventListener('submit', function (e) {
        if (!confirm('Are you sure you want to deactivate your account? This action cannot be undone.')) {
            e.preventDefault();
        }
    });

    // Toast notification with animation
    const success = '<%= request.getAttribute("success") %>';
    const passwordSuccess = '<%= request.getAttribute("success-password") %>';
    if (success === 'update_success' || passwordSuccess === 'password_changed_successfully') {
        const toast = document.getElementById('successToast');
        toast.classList.add('show');
        setTimeout(() => {
            toast.classList.remove('show');
        }, 3500);
    }

    // Animate elements with delay
    document.querySelectorAll('[data-animate-delay]').forEach(el => {
        const delay = el.getAttribute('data-animate-delay');
        el.style.animationDelay = delay;
    });

    // Interactive effects
    document.querySelectorAll('.btn-custom, .btn-delete').forEach(btn => {
        btn.addEventListener('mouseenter', () => {
            btn.style.transform = 'translateY(-3px) scale(1.02)';
            btn.style.boxShadow = '0 5px 15px rgba(0, 0, 0, 0.2)';
        });
        btn.addEventListener('mouseleave', () => {
            btn.style.transform = 'translateY(0) scale(1)';
            btn.style.boxShadow = 'none';
        });
    });

    document.querySelectorAll('.form-control').forEach(input => {
        input.addEventListener('focus', () => {
            input.parentElement.querySelector('.form-label').style.color = '#007bff';
        });
        input.addEventListener('blur', () => {
            input.parentElement.querySelector('.form-label').style.color = '#1e2a44';
        });
    });

    document.querySelectorAll('.collapse-section').forEach(section => {
        section.querySelector('.btn').addEventListener('click', () => {
            const collapse = section.querySelector('.collapse');
            if (!collapse.classList.contains('show')) {
                collapse.classList.add('animate__animated', 'animate__fadeInUp');
            } else {
                collapse.classList.remove('animate__animated', 'animate__fadeInUp');
            }
        });
    });
</script>
</body>
</html>