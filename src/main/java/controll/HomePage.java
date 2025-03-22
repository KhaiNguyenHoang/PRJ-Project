package controll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Account;
import model.Members;

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
        // Check if the session contains a logged-in member
        Members loggedInMember = (Members) request.getSession().getAttribute("members");

        if (loggedInMember != null) {
            // If logged in as member, forward to member homepage
            request.getRequestDispatcher("HomeHTML/HomeMemberHTML/index.jsp").forward(request, response);
        } else {
            // Check if the session contains a logged-in staff account
            Account loggedInAccount = (Account) request.getSession().getAttribute("user");

            if (loggedInAccount != null) {
                // If logged in as staff, forward to staff homepage
                request.getRequestDispatcher("HomeHTML/HomeStaffHTML/index.jsp").forward(request, response);
            } else {
                // If not logged in, forward to sign-in page
                request.getRequestDispatcher("/Auth/SignIn-SignUp.jsp").forward(request, response);
            }
        }
    }
}
