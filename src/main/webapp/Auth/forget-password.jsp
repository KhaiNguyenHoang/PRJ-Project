<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Forget Password</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link href="https://fonts.googleapis.com/css2?family=Poppins:wght@300;500;600&display=swap" rel="stylesheet">
    <style>
        /* Same CSS as your provided signup.jsp - I'll only include the essential parts */
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

        .container {
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
            position: relative;
            overflow: hidden;
            width: 400px;
            max-width: 100%;
            min-height: 480px;
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

        a {
            color: #333;
            font-size: 14px;
            text-decoration: none;
            margin: 15px 0;
        }

        .forgot:hover {
            color: #3c97bf;
        }
    </style>
</head>
<body>
<div class="container">
    <form action="ForgotPassword" method="post">
        <h1>Reset Password</h1>

        <input type="email" name="email" placeholder="Email" required/>
        <input type="password" name="newPassword" placeholder="New Password" required/>
        <input type="password" name="confirmPassword" placeholder="Confirm New Password" required/>

        <!-- Error Messages -->
        <%
            String error = (String) request.getAttribute("error");
            if (error != null) {
                if (error.equals("missing_fields")) {
        %>
        <div class="error-message" style="color: #FF4B2B; font-family:'Comic Sans MS'; padding-bottom: 5px">
            Please fill in all fields.
        </div>
        <%
        } else if (error.equals("password_mismatch")) {
        %>
        <div class="error-message" style="color: #FF4B2B; font-family:'Comic Sans MS'; padding-bottom: 5px">
            Passwords do not match.
        </div>
        <%
        } else if (error.equals("email_not_found")) {
        %>
        <div class="error-message" style="color: #FF4B2B; font-family:'Comic Sans MS'; padding-bottom: 5px">
            Email not found in our system.
        </div>
        <%
                }
            }
        %>

        <button type="submit">Reset Password</button>
        <a class="forgot" href="HomePage">Back to Login</a>
    </form>
</div>
</body>
</html>