package controller;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LogourServlet", value = "/logout")
public class LogoutServlet extends HttpServlet {
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Xóa session
        request.getSession().removeAttribute("user");
        request.getSession().invalidate();
        try {
            request.getRequestDispatcher("/Auth/SignIn-SignUp.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
