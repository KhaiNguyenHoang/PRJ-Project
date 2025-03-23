package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "controll.HomeAuth", value = "/homeAuth")
public class HomeAuth extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }

    @Override

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
        } catch (ServletException | IOException e) {
            e.printStackTrace();
        }
    }
}