package controller.staffController;

import dao.AccountDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "AddStaffServlet", value = "/AddStaff")
public class AddStaffServlet extends HttpServlet {
    private static final Logger LOGGER = Logger.getLogger(AddStaffServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        LOGGER.info("Displaying AddStaff form.");
        request.getRequestDispatcher("HomeHTML/HomeAdminHTML/AddStaff.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Lấy dữ liệu từ form
        String fullName = request.getParameter("fullName");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String roleIdStr = request.getParameter("role_id");

        // Kiểm tra dữ liệu đầu vào
        if (fullName == null || fullName.trim().isEmpty() ||
                email == null || email.trim().isEmpty() ||
                username == null || username.trim().isEmpty() ||
                password == null || password.trim().isEmpty() ||
                roleIdStr == null || roleIdStr.trim().isEmpty()) {
            LOGGER.warning("Invalid input data received.");
            request.setAttribute("error", "All fields are required.");
            request.getRequestDispatcher("HomeHTML/HomeAdminHTML/AddStaff.jsp").forward(request, response);
            return;
        }

        int roleId;
        try {
            roleId = Integer.parseInt(roleIdStr);
            if (roleId != 2) { // Chỉ cho phép Role_ID = 2 (Staff)
                LOGGER.warning("Invalid Role_ID: " + roleId);
                request.setAttribute("error", "Invalid role ID for staff.");
                request.getRequestDispatcher("HomeHTML/HomeAdminHTML/AddStaff.jsp").forward(request, response);
                return;
            }
        } catch (NumberFormatException e) {
            LOGGER.log(Level.SEVERE, "Invalid role_id format: " + roleIdStr, e);
            request.setAttribute("error", "Invalid role ID format.");
            request.getRequestDispatcher("HomeHTML/HomeAdminHTML/AddStaff.jsp").forward(request, response);
            return;
        }

        // Kiểm tra độ dài mật khẩu
        if (password.length() < 6) {
            LOGGER.warning("Password too short for username: " + username);
            request.setAttribute("error", "Password must be at least 6 characters.");
            request.getRequestDispatcher("HomeHTML/HomeAdminHTML/AddStaff.jsp").forward(request, response);
            return;
        }

        // Thực hiện đăng ký tài khoản
        AccountDAO accountDAO = new AccountDAO();
        try {
            boolean success = accountDAO.registerAccount(fullName, email, username, password, roleId);
            if (success) {
                LOGGER.info("Staff added successfully with email: " + email);
                response.sendRedirect("AdminDashboard.jsp?success=Staff+added+successfully");
            } else {
                LOGGER.warning("Failed to add staff. Username or email may already exist: " + username + ", " + email);
                String errorMessage;
                if (accountDAO.checkUsernameExists(username)) {
                    errorMessage = "Username '" + username + "' already exists.";
                } else if (accountDAO.checkEmailExists(email)) {
                    errorMessage = "Email '" + email + "' already exists.";
                } else {
                    errorMessage = "Failed to add staff due to an unexpected error.";
                }
                request.setAttribute("error", errorMessage);
                request.getRequestDispatcher("HomeHTML/HomeAdminHTML/AddStaff.jsp").forward(request, response);
            }
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error while adding staff: " + e.getMessage(), e);
            request.setAttribute("error", "An unexpected error occurred. Please try again later.");
            request.getRequestDispatcher("HomeHTML/HomeAdminHTML/AddStaff.jsp").forward(request, response);
        }
    }
}