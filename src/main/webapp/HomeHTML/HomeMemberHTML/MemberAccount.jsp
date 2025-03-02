<%@ page import="model.entity.Members" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8"/>
    <meta http-equiv="X-UA-Compatible" content="IE=edge"/>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no"/>
    <title>Quản Lý Tài Khoản</title>

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap" rel="stylesheet"/>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <link href="https://unpkg.com/aos@2.3.1/dist/aos.css" rel="stylesheet"/>
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.3/css/all.min.css" rel="stylesheet"/>

    <style>
        /* General Styles */
        body {
            background: linear-gradient(135deg, #e74c3c, #8e44ad);
            font-family: 'Roboto', sans-serif;
            color: white;
        }

        /* Navbar Styles */
        .navbar {
            background-color: rgba(0, 0, 0, 0.9);
            padding: 20px;
        }

        .navbar-nav .nav-link {
            color: white;
            font-size: 1.2rem;
            padding: 10px 15px;
            font-weight: bold;
            transition: all 0.3s ease;
        }

        .navbar-nav .nav-link:hover {
            color: #00bcd4;
            transform: scale(1.1);
        }

        .navbar-brand {
            color: white;
            font-size: 1.8rem;
            font-weight: bold;
            text-transform: uppercase;
        }

        /* Account Section */
        .account-section {
            padding: 60px 0;
        }

        .card {
            border-radius: 20px;
            box-shadow: 0 20px 50px rgba(0, 0, 0, 0.1);
            margin-bottom: 30px;
        }

        .card-header {
            background-color: #00bcd4;
            color: white;
            font-size: 1.5rem;
            text-align: center;
            padding: 20px;
            border-radius: 20px 20px 0 0;
        }

        .card-body {
            background-color: #f4f6f9;
            padding: 30px;
            font-size: 1.1rem;
            border-radius: 0 0 20px 20px;
        }

        .form-control {
            border-radius: 10px;
            padding: 16px;
            font-size: 1rem;
            border: 1px solid #ddd;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            border-color: #00bcd4;
            box-shadow: 0 0 8px rgba(0, 188, 212, 0.3);
        }

        .btn-custom {
            background-color: #00bcd4;
            color: white;
            width: 100%;
            padding: 12px;
            border-radius: 12px;
            font-size: 1.2rem;
            transition: 0.3s;
            border: none;
        }

        .btn-custom:hover {
            background-color: #00acc1;
            transform: translateY(-3px);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
        }

        .btn-delete {
            background-color: #e74c3c;
            color: white;
            width: 100%;
            padding: 12px;
            border-radius: 12px;
            font-size: 1.2rem;
            margin-top: 20px;
            transition: 0.3s;
            border: none;
        }

        .btn-delete:hover {
            background-color: #c0392b;
            transform: translateY(-3px);
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.15);
        }

        .toast {
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            background-color: #28a745;
            color: white;
            padding: 10px;
            border-radius: 12px;
            display: none;
            font-size: 1.2rem;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
            transform: translateX(100%);
            transition: all 0.5s ease;
        }

        .toast.show {
            transform: translateX(0);
        }

        .toast i {
            margin-right: 8px;
        }

        /* Footer Section */
        .footer-section {
            background-color: #333;
            color: white;
            text-align: center;
            padding: 20px 0;
            margin-top: 40px;
        }

        .footer-section p {
            font-size: 1rem;
            margin: 0;
        }
    </style>
</head>
<body data-aos="fade-in">

<!-- Navbar -->
<header class="header_section">
    <div class="container">
        <nav class="navbar navbar-expand-lg navbar-dark">
            <a class="navbar-brand" href="index.html">Library</a>
            <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent">
                <span class="navbar-toggler-icon"></span>
            </button>
            <div class="collapse navbar-collapse" id="navbarSupportedContent">
                <ul class="navbar-nav ml-auto">
                    <li class="nav-item">
                        <a class="nav-link" href="HomePage">Home</a>
                    </li>
                    <li class="nav-item active">
                        <a class="nav-link" href="MemberAccount">My Account</a>
                    </li>
                    <li class="nav-item">
                        <a class="nav-link" href="logoutauth">Logout</a>
                    </li>
                </ul>
            </div>
        </nav>
    </div>
</header>

<!-- Account Management Section -->
<div class="container account-section">
    <div class="row justify-content-center">
        <div class="col-lg-8 col-md-10 col-sm-12">
            <div class="card" data-aos="flip-left">
                <div class="card-header">
                    <span><i class="fas fa-user-circle"></i> Quản Lý Tài Khoản</span>
                </div>
                <div class="card-body">
                    <form action="UpdateMember" method="post">
                        <%
                            Members loggedInMember = (Members) session.getAttribute("user");
                            if (loggedInMember != null) {
                        %>

                        <div class="mb-3">
                            <label for="fullName" class="form-label">Họ và Tên:</label>
                            <input type="text" id="fullName" name="fullName" class="form-control"
                                   value="<%= loggedInMember.getFullName() %>" required/>
                        </div>

                        <div class="mb-3">
                            <label for="email" class="form-label">Email:</label>
                            <input type="email" id="email" name="email" class="form-control"
                                   value="<%= loggedInMember.getEmail() %>" readonly/>
                        </div>

                        <div class="mb-3">
                            <label for="phone" class="form-label">Số Điện Thoại:</label>
                            <input type="text" id="phone" name="phone" class="form-control"
                                   value="<%= loggedInMember.getPhone() %>" required/>
                        </div>

                        <div class="mb-3">
                            <label for="address" class="form-label">Địa Chỉ:</label>
                            <input type="text" id="address" name="address" class="form-control"
                                   value="<%= loggedInMember.getAddress() %>" required/>
                        </div>

                        <div class="mb-3">
                            <label for="oldPassword" class="form-label">Mật khẩu hiện tại:</label>
                            <div class="input-group">
                                <input type="password" id="oldPassword" name="oldPassword" class="form-control"
                                       placeholder="Nhập mật khẩu cũ"/>
                                <span class="input-group-text" onclick="togglePassword('oldPassword')"><i
                                        class="fas fa-eye"></i></span>
                            </div>
                        </div>

                        <div class="mb-3">
                            <label for="newPassword" class="form-label">Mật khẩu mới:</label>
                            <div class="input-group">
                                <input type="password" id="newPassword" name="newPassword" class="form-control"
                                       placeholder="Nhập mật khẩu mới"/>
                                <span class="input-group-text" onclick="togglePassword('newPassword')"><i
                                        class="fas fa-eye"></i></span>
                            </div>
                        </div>

                        <input type="hidden" name="idMember" value="<%= loggedInMember.getIdMember() %>"/>

                        <button type="submit" class="btn-custom">Lưu Thay Đổi</button>
                    </form>

                    <form action="DeleteAccount" method="post">
                        <input type="hidden" name="idMember" value="<%= loggedInMember.getIdMember() %>"/>
                        <div class="mb-3">
                            <label for="confirmPassword" class="form-label">Nhập mật khẩu để xóa tài khoản:</label>
                            <input type="password" id="confirmPassword" name="confirmPassword" class="form-control"
                                   required/>
                        </div>

                        <button type="submit" class="btn-delete"
                                onclick="return confirm('Bạn có chắc muốn xóa tài khoản không?')">Xóa Tài Khoản
                        </button>
                    </form>

                    <%
                    } else {
                    %>
                    <p class="text-center text-danger">Bạn chưa đăng nhập.</p>
                    <%
                        }
                    %>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- Toast Notification -->
<div class="toast" id="toastMessage">Cập nhật thành công!</div>

<!-- Footer Section -->
<section class="footer-section">
    <p>© 2024 Group 2 All Rights Reserved | Design by Group 2</p>
</section>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
<script src="https://unpkg.com/aos@2.3.1/dist/aos.js"></script>
<script>
    function togglePassword(id) {
        let input = document.getElementById(id);
        if (input.type === "password") {
            input.type = "text";
        } else {
            input.type = "password";
        }
    }

    $(document).ready(function () {
        let successMessage = '<%= request.getAttribute("successMessage") %>';
        if (successMessage !== "null") {
            $("#toastMessage").text(successMessage).fadeIn().delay(3000).fadeOut();
        }
    });

    AOS.init();
</script>

</body>
</html>
