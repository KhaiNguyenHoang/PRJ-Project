package controll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.AccountDAO;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final String REGISTER_PAGE = "Auth/SignIn-SignUp.jsp";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullNameStaff");
        String email = request.getParameter("emailStaff");
        String password = request.getParameter("passwordStaff");
        String username = request.getParameter("usernameStaff");
        String roleIdStr = request.getParameter("roleIdStaff");

        if (fullName == null || email == null || password == null || username == null || roleIdStr == null) {
            request.setAttribute(ERROR_ATTRIBUTE, "missing_fields");
            try {
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            } catch (ServletException | IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            // Chuyển đổi roleId và xử lý
            int roleId = Integer.parseInt(roleIdStr);
            if (roleId != 1 && roleId != 2) {
                request.setAttribute(ERROR_ATTRIBUTE, "invalid_role_id");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            AccountDAO accountDAO = new AccountDAO();
            if (accountDAO.checkEmailExists(email) || accountDAO.checkUsernameExists(username)) {
                request.setAttribute(ERROR_ATTRIBUTE, "email_or_username_exists");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            // Đăng ký tài khoản mới
            accountDAO.registerAccount(fullName, email, username, password, roleId);

            // Chuyển hướng thành công
            request.setAttribute("success", "account_created");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE, "invalid_role_id");
            try {
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        } catch (Exception e) {
            LOGGER.severe("Error occurred during registration: " + e.getMessage());
            request.setAttribute(ERROR_ATTRIBUTE, "internal_error");
            try {
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            } catch (ServletException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
