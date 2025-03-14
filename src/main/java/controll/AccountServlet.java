package controll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.MembersDAO;
import model.entity.Members;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@WebServlet(name = "AccountServlet", value = "/Account")
public class AccountServlet extends HttpServlet {
    private static final String ERROR_ATTRIBUTE = "error";

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        String action = request.getParameter("action");

        if ("changeInfo".equalsIgnoreCase(action)) {
            String fullName = request.getParameter("fullName").trim();
            String email = request.getParameter("email").trim();
            String phone = request.getParameter("phone").trim();
            String address = request.getParameter("address").trim();

            // Check if any field is empty
            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                request.setAttribute(ERROR_ATTRIBUTE, "empty_field");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            // Validate email format
            if (!isValidEmail(email)) {
                request.setAttribute(ERROR_ATTRIBUTE, "invalid_email");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            // Validate phone number format
            if (!isValidPhone(phone)) {
                request.setAttribute(ERROR_ATTRIBUTE, "invalid_phone");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            // Update user information
            Members loggedInMember = (Members) request.getSession().getAttribute("user");
            if (loggedInMember == null) {
                response.sendRedirect("/Auth/SignIn-SignUp.jsp");
                return;
            }

            // Update the member object
            loggedInMember.setFullName(fullName);
            loggedInMember.setEmail(email);
            loggedInMember.setPhone(phone);
            loggedInMember.setAddress(address);

            MembersDAO membersDAO = new MembersDAO();
            boolean updateSuccess = membersDAO.updateMember(loggedInMember); // Now returns boolean

            if (updateSuccess) {
                // Refresh session with updated member data from database
                Members updatedMember = membersDAO.getMemberById(loggedInMember.getIdMember());
                request.getSession().setAttribute("user", updatedMember);
                request.setAttribute("success", "update_success");
            } else {
                request.setAttribute(ERROR_ATTRIBUTE, "update_failed");
            }
            request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile(emailRegex);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }
}