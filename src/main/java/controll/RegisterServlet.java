package controll;

import dao.AccountDAO;
import dao.MembersDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "RegisterServlet", value = "/RegisterServlet")
public class RegisterServlet extends HttpServlet {
    private static final String REGISTER_PAGE = "Auth/SignIn-SignUp.jsp";
    private static final String ERROR_ATTRIBUTE = "error";
    private static final Logger LOGGER = Logger.getLogger(RegisterServlet.class.getName());

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String accountType = request.getParameter("accountType");

        if (accountType == null || (!accountType.equals("member") && !accountType.equals("staff"))) {
            request.setAttribute(ERROR_ATTRIBUTE, "missing_fields");
            request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            return;
        }

        if (accountType.equals("member")) {
            String fullName = request.getParameter("fullNameMember").trim();
            String email = request.getParameter("emailMember").trim();
            String phone = request.getParameter("phoneMember").trim();
            String address = request.getParameter("addressMember").trim();
            String password = request.getParameter("passwordMember").trim();

            if (fullName.isEmpty() || email.isEmpty() || phone.isEmpty() || address.isEmpty() || password.isEmpty()) {
                request.setAttribute(ERROR_ATTRIBUTE, "missing_fields");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            if (!isValidEmail(email)) {
                request.setAttribute(ERROR_ATTRIBUTE, "invalid_email");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            MembersDAO membersDAO = new MembersDAO();
            if (membersDAO.checkEmailExists(email)) {
                request.setAttribute(ERROR_ATTRIBUTE, "email_or_username_exists");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            try {
                membersDAO.registerMember(fullName, email, phone, address, password);
                request.setAttribute("success", "account_created");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            } catch (Exception e) {
                LOGGER.severe("Failed to register member: " + e.getMessage());
                request.setAttribute(ERROR_ATTRIBUTE, "internal_error");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            }
        } else {
            String fullName = request.getParameter("fullNameStaff").trim();
            String email = request.getParameter("emailStaff").trim();
            String username = request.getParameter("usernameStaff").trim();
            String password = request.getParameter("passwordStaff").trim();
            String roleIdStr = request.getParameter("roleIdStaff");

            if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty() || roleIdStr == null) {
                request.setAttribute(ERROR_ATTRIBUTE, "missing_fields");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            if (!isValidEmail(email)) {
                request.setAttribute(ERROR_ATTRIBUTE, "invalid_email");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
                return;
            }

            try {
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

                accountDAO.registerAccount(fullName, email, username, password, roleId);
                request.setAttribute("success", "account_created");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            } catch (NumberFormatException e) {
                request.setAttribute(ERROR_ATTRIBUTE, "invalid_role_id");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            } catch (Exception e) {
                LOGGER.severe("Failed to register staff/admin: " + e.getMessage());
                request.setAttribute(ERROR_ATTRIBUTE, "internal_error");
                request.getRequestDispatcher(REGISTER_PAGE).forward(request, response);
            }
        }
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        return email.matches(emailRegex);
    }
}