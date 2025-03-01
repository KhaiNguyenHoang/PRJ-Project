package controll;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.dao.AccountDAO;
import model.dao.MembersDAO;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final String REGISTER_PAGE = "Auth/SignIn-SignUp.jsp";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountType = "staff";

        if ((!request.getParameter("fullNameMember").isEmpty() && !request.getParameter("emailMember").isEmpty() && !request.getParameter("phoneMember").isEmpty() && !request.getParameter("addressMember").isEmpty() && !request.getParameter("passwordMember").isEmpty())) {
            accountType = "member";
        }

        // Nếu là member
        if (accountType.equals("member")) {
            String fullName = request.getParameter("fullNameMember");
            String email = request.getParameter("emailMember");
            String phone = request.getParameter("phoneMember");
            String address = request.getParameter("addressMember");
            String password = request.getParameter("passwordMember");

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
                request.setAttribute(ERROR_ATTRIBUTE, "missing_fields");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            MembersDAO membersDAO = new MembersDAO();
            if (membersDAO.checkEmailExists(email)) {
                request.setAttribute(ERROR_ATTRIBUTE, "email_or_username_exists");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            // Đăng ký Member
            membersDAO.registerMember(fullName, email, phone, address, password);

            // Thành công
            request.setAttribute("success", "account_created");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }
        // Nếu là staff/admin
        String fullName = request.getParameter("fullNameStaff");
        String email = request.getParameter("emailStaff");
        String username = request.getParameter("usernameStaff");
        String password = request.getParameter("passwordStaff");
        String roleIdStr = request.getParameter("roleIdStaff");

        if (fullName.trim().isEmpty() || email.trim().isEmpty() || username.trim().isEmpty() || password.trim().isEmpty() || roleIdStr.trim().isEmpty()) {
            request.setAttribute(ERROR_ATTRIBUTE, "missing_fields");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }

        try {
            // Chuyển đổi roleId
            int roleId = Integer.parseInt(roleIdStr);
            if (roleId != 3 && roleId != 4) {
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

            // Đăng ký Staff/Admin
            accountDAO.registerAccount(fullName, email, username, password, roleId);

            // Thành công
            request.setAttribute("success", "account_created");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute(ERROR_ATTRIBUTE, "invalid_role_id");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
        }
    }
}