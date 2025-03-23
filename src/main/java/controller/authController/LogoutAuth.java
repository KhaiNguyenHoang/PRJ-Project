package controller.authController;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "LogoutAuth", value = "/logoutauth")
public class LogoutAuth extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Forward to the logout page
            request.getRequestDispatcher("/Auth/Logout.jsp").forward(request, response);
        } catch (Exception e) {
            Logger.getLogger(LogoutAuth.class.getName()).log(java.util.logging.Level.SEVERE, null, e);
        }
    }
}
