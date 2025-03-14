package controll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.entity.Members;

import java.io.IOException;

@WebServlet(name = "HomePage", value = "/HomePage")
public class HomePage extends HttpServlet {

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        forwardToHomePage(request, response);
    }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        forwardToHomePage(request, response);
    }

    private void forwardToHomePage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Members loggedInMember = (Members) request.getSession().getAttribute("user");
        if (loggedInMember != null) {
            try {
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/index.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                request.getRequestDispatcher("/Auth/SignIn-SignUp.jsp").forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
        }
    }
}
