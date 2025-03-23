package controller.accountController;

import dao.MembersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Members;

import java.io.IOException;

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

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                request.setAttribute(ERROR_ATTRIBUTE, "empty_field");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            if (!isValidEmail(email)) {
                request.setAttribute(ERROR_ATTRIBUTE, "invalid_email");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            if (!isValidPhone(phone)) {
                request.setAttribute(ERROR_ATTRIBUTE, "invalid_phone");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            Members loggedInMember = (Members) request.getSession().getAttribute("user");
            if (loggedInMember == null) {
                response.sendRedirect("HomePage");
                return;
            }

            loggedInMember.setFullName(fullName);
            loggedInMember.setEmail(email);
            loggedInMember.setPhone(phone);
            loggedInMember.setAddress(address);

            MembersDAO membersDAO = new MembersDAO();
            boolean updateSuccess = membersDAO.updateMember(loggedInMember);

            if (updateSuccess) {
                Members updatedMember = membersDAO.getMemberById(loggedInMember.getIdMember());
                request.getSession().setAttribute("user", updatedMember);
                request.setAttribute("success", "update_success");
            } else {
                request.setAttribute(ERROR_ATTRIBUTE, "update_failed");
            }
            request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
        } else if ("changePassword".equalsIgnoreCase(action)) {
            String currentPassword = request.getParameter("currentPassword").trim();
            String newPassword = request.getParameter("newPassword").trim();
            String confirmNewPassword = request.getParameter("confirmNewPassword").trim();

            Members loggedInMember = (Members) request.getSession().getAttribute("user");
            if (loggedInMember == null) {
                response.sendRedirect("HomePage");
                return;
            }

            if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmNewPassword.isEmpty()) {
                request.setAttribute("error-password", "empty_password_field");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            if (!newPassword.equals(confirmNewPassword)) {
                request.setAttribute("error-password", "password_mismatch");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            MembersDAO membersDAO = new MembersDAO();
            if (!loggedInMember.getPasswordHash().equals(membersDAO.hashPassword(currentPassword))) {
                request.setAttribute("error-password", "incorrect_current_password");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            if (membersDAO.changePassword(currentPassword, newPassword)) {
                request.setAttribute("success-password", "password_changed_successfully");
            } else {
                request.setAttribute("error-password", "password_change_failed");
            }
            request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
        } else if ("deactiveAccount".equalsIgnoreCase(action)) {
            // Get the confirmation password from parameters, not attributes
            String confirmPassword = request.getParameter("confirmPassword");

            // Retrieve logged-in member
            Members loggedInMember = (Members) request.getSession().getAttribute("user");
            if (loggedInMember == null) {
                response.sendRedirect("HomePage");
                return;
            }

            // Validate password field
            if (confirmPassword == null || confirmPassword.trim().isEmpty()) {
                request.setAttribute("error-deactive", "empty_password_field");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            MembersDAO membersDAO = new MembersDAO();

            // Verify the password matches the stored hash
            String hashedConfirmPassword = membersDAO.hashPassword(confirmPassword);
            if (!loggedInMember.getPasswordHash().equals(hashedConfirmPassword)) {
                request.setAttribute("error-deactive", "incorrect_password");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            // Deactivate the account using the provided method
            membersDAO.deactiveAccount(loggedInMember.getIdMember());

            // Clear session and set success message
            request.getSession().invalidate();  // Log out the user
            request.setAttribute("success-deactive", "deactive_successfully");
            request.getRequestDispatcher("HomePage").forward(request, response);
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        return email.matches(emailRegex);
    }

    private boolean isValidPhone(String phone) {
        return phone.matches("\\d{10}");
    }
}