package controll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "LogoutAuth", value = "/logoutauth")
public class LogoutAuth extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            // Remove the user attribute and invalidate the session
            request.getSession().removeAttribute("user");
            request.getSession().invalidate();

            // Forward to the logout page
            request.getRequestDispatcher("/Auth/Logout.jsp").forward(request, response);
        } catch (Exception e) {
            // Log the error for debugging purposes
            e.printStackTrace();
            // Optionally, redirect to an error page or return an error message
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Logout failed due to server error.");
        }
    }
}
