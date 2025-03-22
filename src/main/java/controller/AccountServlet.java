package controller;

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

            // Validate that all fields are filled
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

            // Retrieve logged-in member
            Members loggedInMember = (Members) request.getSession().getAttribute("user");
            if (loggedInMember == null) {
                response.sendRedirect("/Auth/SignIn-SignUp.jsp");
                return;
            }

            // Update member information
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

            // Retrieve logged-in member
            Members loggedInMember = (Members) request.getSession().getAttribute("user");
            if (loggedInMember == null) {
                response.sendRedirect("/Auth/SignIn-SignUp.jsp");
                return;
            }

            // Validate password fields
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

            if (!loggedInMember.getPasswordHash().equals(new MembersDAO().hashPassword(currentPassword))) {
                request.setAttribute("error-password", "incorrect_current_password");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            MembersDAO membersDAO = new MembersDAO();

            // Check if current password matches
            if (membersDAO.changePassword(currentPassword, newPassword)) {
                request.setAttribute("success-password", "password_changed_successfully");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
            } else {
                request.setAttribute("error-password", "password_change_failed");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
            }
        } else if ("deactiveAccount".equalsIgnoreCase(action)) {
            String passwordHash = request.getAttribute("confirmPassword").toString();
            // Retrieve logged-in member
            Members loggedInMember = (Members) request.getSession().getAttribute("user");
            if (loggedInMember == null) {
                response.sendRedirect("/Auth/SignIn-SignUp.jsp");
                return;
            }

            // Validate password field
            if (passwordHash.isEmpty()) {
                request.setAttribute("error-deactive", "empty_password_field");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
                return;
            }

            MembersDAO membersDAO = new MembersDAO();
            // Check if current password matches
            if (membersDAO.deactiveAccount(loggedInMember.getIdMember(), passwordHash)) {
                request.setAttribute("success-deactive", "deactive_successfully");
                response.setHeader("Refresh", "10; URL=/Auth/SignIn-SignUp.jsp"); // Redirect after 10 seconds
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
            } else {
                request.setAttribute("error-deactive", "deactive_failed");
                request.getRequestDispatcher("HomeHTML/HomeMemberHTML/MemberAccount.jsp").forward(request, response);
            }
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