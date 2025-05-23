<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sign Up</title>
    <link rel="preconnect" href="https://fonts.gstatic.com">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;600&display=swap" rel="stylesheet">
    <style media="screen">
        @import url('https://fonts.googleapis.com/css?family=Montserrat:400,800');

        * {
            box-sizing: border-box;
        }

        body {
            background: #f6f5f7;
            display: flex;
            justify-content: center;
            align-items: center;
            flex-direction: column;
            font-family: 'Montserrat', sans-serif;
            height: 100vh;
            margin: -20px 0 50px;
        }

        h1 {
            font-weight: bold;
            margin: 0;
        }

        h2 {
            text-align: center;
        }

        p {
            font-size: 14px;
            font-weight: 100;
            line-height: 20px;
            letter-spacing: 0.5px;
            margin: 20px 0 30px;
        }

        span {
            font-size: 12px;
        }

        a {
            color: #333;
            font-size: 14px;
            text-decoration: none;
            margin: 15px 0;
        }

        /* CSS cho dropdown chọn loại tài khoản */
        .account-type-select {
            width: 100%;
            padding: 12px 15px;
            margin: 8px 0;
            border: 1px solid #ddd;
            border-radius: 25px; /* Bo tròn góc */
            background-color: #eee;
            font-size: 14px;
            font-family: 'Poppins', sans-serif;
            color: #333;
            appearance: none; /* Ẩn mũi tên mặc định */
            -webkit-appearance: none; /* Dành cho trình duyệt WebKit */
            -moz-appearance: none; /* Dành cho trình duyệt Firefox */
            background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%23333' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
            background-repeat: no-repeat;
            background-position: right 12px center;
            background-size: 16px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        /* Hiệu ứng khi hover */
        .account-type-select:hover {
            border-color: #FF4B2B;
            background-color: #fff;
        }

        /* Hiệu ứng khi focus */
        .account-type-select:focus {
            outline: none;
            border-color: #FF4B2B;
            box-shadow: 0 0 5px rgba(255, 75, 43, 0.5);
        }

        /* Tùy chỉnh option */
        .account-type-select option {
            padding: 10px;
            background-color: #fff;
            color: #333;
            font-family: 'Poppins', sans-serif;
        }

        .role-type-select {
            width: 100%;
            padding: 12px 15px;
            margin: 8px 0;
            border: 1px solid #ddd;
            background-color: #eee;
            font-size: 14px;
            font-family: 'Poppins', sans-serif;
            color: #333;
            appearance: none; /* Ẩn mũi tên mặc định */
            -webkit-appearance: none; /* Dành cho trình duyệt WebKit */
            -moz-appearance: none; /* Dành cho trình duyệt Firefox */
            background-image: url("data:image/svg+xml;charset=UTF-8,%3csvg xmlns='http://www.w3.org/2000/svg' viewBox='0 0 24 24' fill='none' stroke='%23333' stroke-width='2' stroke-linecap='round' stroke-linejoin='round'%3e%3cpolyline points='6 9 12 15 18 9'%3e%3c/polyline%3e%3c/svg%3e");
            background-repeat: no-repeat;
            background-position: right 12px center;
            background-size: 16px;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .role-type-select option {
            padding: 10px;
            background-color: #fff;
            color: #333;
            font-family: 'Poppins', sans-serif;
        }

        /* Hiệu ứng khi hover */
        .role-type-select:hover {
            border-color: #FF4B2B;
            background-color: #fff;
        }

        /* Hiệu ứng khi focus */
        .role-type-select:focus {
            outline: none;
            border-color: #FF4B2B;
            box-shadow: 0 0 5px rgba(255, 75, 43, 0.5);
        }

        button {
            border-radius: 20px;
            border: 1px solid #FF4B2B;
            background-color: #FF4B2B;
            color: #FFFFFF;
            font-size: 12px;
            font-weight: bold;
            padding: 12px 45px;
            letter-spacing: 1px;
            text-transform: uppercase;
            transition: transform 80ms ease-in;
        }

        button:active {
            transform: scale(0.95);
        }

        button:focus {
            outline: none;
        }

        button.ghost {
            background-color: transparent;
            border-color: #FFFFFF;
        }

        form {
            background-color: #FFFFFF;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            padding: 0 50px;
            height: 100%;
            text-align: center;
        }

        input {
            background-color: #eee;
            border: none;
            padding: 12px 15px;
            margin: 8px 0;
            width: 100%;
        }

        .container {
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25),
            0 10px 10px rgba(0, 0, 0, 0.22);
            position: relative;
            overflow: hidden;
            width: 768px;
            max-width: 100%;
            min-height: 480px;
        }

        .form-container {
            position: absolute;
            top: 0;
            height: 100%;
            transition: all 0.6s ease-in-out;
        }

        .sign-in-container {
            left: 0;
            width: 50%;
            z-index: 2;
        }

        .container.right-panel-active .sign-in-container {
            transform: translateX(100%);
        }

        .sign-up-container {
            left: 0;
            width: 50%;
            opacity: 0;
            z-index: 1;
        }

        .container.right-panel-active .sign-up-container {
            transform: translateX(100%);
            opacity: 1;
            z-index: 5;
            animation: show 0.6s;
        }

        @keyframes show {
            0%, 49.99% {
                opacity: 0;
                z-index: 1;
            }

            50%, 100% {
                opacity: 1;
                z-index: 5;
            }
        }

        .overlay-container {
            position: absolute;
            top: 0;
            left: 50%;
            width: 50%;
            height: 100%;
            overflow: hidden;
            transition: transform 0.6s ease-in-out;
            z-index: 100;
        }

        .container.right-panel-active .overlay-container {
            transform: translateX(-100%);
        }

        .overlay {
            background: #FF416C;
            background: -webkit-linear-gradient(to right, #FF4B2B, #FF416C);
            background: linear-gradient(to right, #FF4B2B, #FF416C);
            background-repeat: no-repeat;
            background-size: cover;
            background-position: 0 0;
            color: #FFFFFF;
            position: relative;
            left: -100%;
            height: 100%;
            width: 200%;
            transform: translateX(0);
            transition: transform 0.6s ease-in-out;
        }

        .container.right-panel-active .overlay {
            transform: translateX(50%);
        }

        .overlay-panel {
            position: absolute;
            display: flex;
            align-items: center;
            justify-content: center;
            flex-direction: column;
            padding: 0 40px;
            text-align: center;
            top: 0;
            height: 100%;
            width: 50%;
            transform: translateX(0);
            transition: transform 0.6s ease-in-out;
        }

        .overlay-left {
            transform: translateX(-20%);
        }

        .container.right-panel-active .overlay-left {
            transform: translateX(0);
        }

        .overlay-right {
            right: 0;
            transform: translateX(0);
        }

        .container.right-panel-active .overlay-right {
            transform: translateX(20%);
        }

        .social-container {
            margin: 20px 0;
        }

        .social-container a {
            border: 1px solid #DDDDDD;
            border-radius: 50%;
            display: inline-flex;
            justify-content: center;
            align-items: center;
            margin: 0 5px;
            height: 40px;
            width: 40px;
        }

        footer {
            background-color: #222;
            color: #fff;
            font-size: 14px;
            bottom: 0;
            position: fixed;
            left: 0;
            right: 0;
            text-align: center;
            z-index: 999;
        }

        footer p {
            margin: 10px 0;
        }

        footer i {
            color: red;
        }

        footer a {
            color: #3c97bf;
            text-decoration: none;
        }

        .forgot:hover {
            color: #3c97bf;
        }
    </style>
</head>
<body>
<div class="container" id="container">
    <!-- Phần đăng ký -->
    <div class="form-container sign-in-container">
        <form action="logout" method="post">
            <h1>Log out</h1>
            <p>Are you sure you want to log out?</p>
            <button type="submit">Log out</button>
        </form>
    </div>

    <!-- Phần đăng nhập -->
    <div class="form-container sign-up-container">
        <form action="CancelLogout" method="post">
            <h1>Cancel log out</h1>
            <p>Are you sure you want to cancel log out?</p>
            <button type="submit">Cancel</button>
        </form>
    </div>


    <!-- Overlay -->
    <div class="overlay-container">
        <div class="overlay">
            <div class="overlay-panel overlay-right">
                <h1>Keep work with us!</h1>
                <p>Comeback the home page with us</p>
                <button class="ghost" id="signUp">Cancle the Log out</button>
            </div>
            <div class="overlay-panel overlay-left">
                <h1>See you later!</h1>
                <p>To keep connected with us please login with your personal info</p>
                <button class="ghost" id="signIn">Log out</button>
            </div>
        </div>
    </div>
</div>

<script>
    // Script chuyển đổi form
    const signUpButton = document.getElementById('signUp');
    const signInButton = document.getElementById('signIn');
    const container = document.getElementById('container');

    // Mặc định hiển thị form đăng ký
    container.classList.add("right-panel-active");

    signUpButton.addEventListener('click', () => {
        container.classList.add("right-panel-active");
    });

    signInButton.addEventListener('click', () => {
        container.classList.remove("right-panel-active");
    });

    // Script chuyển đổi trường nhập
    document.getElementById('accountType').addEventListener('change', function () {
        const memberFields = document.getElementById('memberFields');
        const staffFields = document.getElementById('staffFields');

        if (this.value === 'member') {
            memberFields.style.display = 'block';
            staffFields.style.display = 'none';
        } else {
            memberFields.style.display = 'none';
            staffFields.style.display = 'block';
        }
    });
</script>
</body>
</html>