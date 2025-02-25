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
    private static final Logger logger = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String fullName = request.getParameter("fullNameStaff");
        String email = request.getParameter("emailStaff");
        String password = request.getParameter("passwordStaff");
        String username = request.getParameter("usernameStaff");
        String roleIdStr = request.getParameter("roleIdStaff");

        if (fullName == null || email == null || password == null || username == null || roleIdStr == null) {
            request.setAttribute("error", "missing_fields");
            request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
            return;
        }

        try {
            // Chuyển đổi roleId và xử lý
            int roleId = Integer.parseInt(roleIdStr);
            if (roleId != 1 && roleId != 2) {
                request.setAttribute("error", "invalid_role_id");
                request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
                return;
            }

            AccountDAO accountDAO = new AccountDAO();
            if (accountDAO.checkEmailExists(email) || accountDAO.checkUsernameExists(username)) {
                request.setAttribute("error", "email_or_username_exists");
                request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
                return;
            }

            // Đăng ký tài khoản mới
            accountDAO.registerAccount(fullName, email, username, password, roleId);

            // Chuyển hướng thành công
            request.setAttribute("success", "account_created");
            request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "invalid_role_id");
            request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
        } catch (Exception e) {
            logger.severe("Error occurred during registration: " + e.getMessage());
            request.setAttribute("error", "internal_error");
            request.getRequestDispatcher("Auth/SignIn-SignUp.jsp").forward(request, response);
        }
    }
}
